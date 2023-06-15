package lol.pyr.znpcsplus.commands.action;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.interaction.InteractionCommandHandler;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActionEditCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;
    private final ActionRegistry actionRegistry;

    private InteractionCommandHandler commandHandler = null;

    public ActionEditCommand(NpcRegistryImpl npcRegistry, ActionRegistry actionRegistry) {
        this.npcRegistry = npcRegistry;
        this.actionRegistry = actionRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " action edit <id> <action index> <action type> ...");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        int index = context.parse(Integer.class);
        if (index >= entry.getNpc().getActions().size() || index < 0) context.halt(Component.text("That npc doesn't have any action with the index " + index, NamedTextColor.RED));
        List<InteractionCommandHandler> commands = actionRegistry.getCommands();
        String sub = context.popString();
        for (InteractionCommandHandler command : commands) if (command.getSubcommandName().equalsIgnoreCase(sub)) {
            this.commandHandler = command;
        }
        if (this.commandHandler == null) {
            context.send(Component.text("Invalid action type, available action types:\n" +
                    commands.stream().map(InteractionCommandHandler::getSubcommandName).collect(Collectors.joining(", ")), NamedTextColor.RED));
        }
        InteractionAction newAction = this.commandHandler.parse(context);
        entry.getNpc().editAction(index, newAction);
        context.send(Component.text("Edited action with index " + index + " of Npc " + entry.getId(), NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestStream(Stream.iterate(0, n -> n + 1)
                .limit(context.suggestionParse(0, NpcEntryImpl.class).getNpc().getActions().size())
                .map(String::valueOf));
        List<InteractionCommandHandler> commands = actionRegistry.getCommands();
        if (context.argSize() == 3) return context.suggestStream(commands.stream().map(InteractionCommandHandler::getSubcommandName));
        context.popString();
        context.popString();
        String sub = context.popString();
        for (InteractionCommandHandler command : commands) if (command.getSubcommandName().equalsIgnoreCase(sub)) return command.suggest(context);
        return Collections.emptyList();
    }
}
