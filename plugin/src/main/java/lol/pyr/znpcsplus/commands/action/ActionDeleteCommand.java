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
import java.util.stream.Stream;

public class ActionDeleteCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public ActionDeleteCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " action delete <id> <index>");
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        int index = context.parse(Integer.class);
        if (index >= npc.getActions().size() || index < 0) context.halt(Component.text("That npc doesn't have any action with the index " + index, NamedTextColor.RED));
        npc.removeAction(index);
        context.send(Component.text("Removed action with index " + index, NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestStream(Stream.iterate(0, n -> n + 1)
                .limit(context.suggestionParse(0, NpcEntryImpl.class).getNpc().getActions().size())
                .map(String::valueOf));
        return Collections.emptyList();
    }
}
