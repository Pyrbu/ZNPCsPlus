package lol.pyr.znpcsplus.commands.action;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class ActionClearCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public ActionClearCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " action clear <id>");
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        if (npc.getActions().size() == 0) context.halt(Component.text("That npc doesn't have any actions", NamedTextColor.RED));
        npc.clearActions();
        context.send(Component.text("Removed all actions from the npc", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        return Collections.emptyList();
    }
}
