package lol.pyr.znpcsplus.commands.action;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.interaction.InteractionActionImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

import java.util.Collections;
import java.util.List;

public class ActionListCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public ActionListCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " action list <id>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        List<InteractionActionImpl> actions = entry.getNpc().getActions();
        context.send("Actions of Npc " + entry.getId() + ":");
        for (int i = 0; i < actions.size(); i++) {
            context.send(actions.get(i).getInfo(entry.getId(), i, context));
        }
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        return Collections.emptyList();
    }
}
