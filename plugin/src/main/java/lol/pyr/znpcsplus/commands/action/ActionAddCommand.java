package lol.pyr.znpcsplus.commands.action;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.interaction.InteractionCommandHandler;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ActionAddCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;
    private final ActionRegistry actionRegistry;

    public ActionAddCommand(NpcRegistryImpl npcRegistry, ActionRegistry actionRegistry) {
        this.npcRegistry = npcRegistry;
        this.actionRegistry = actionRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        List<InteractionCommandHandler> commands = actionRegistry.getCommands();
        context.setUsage(context.getLabel() + " action add <action type> ...");
        String sub = context.popString();
        for (InteractionCommandHandler command : commands) if (command.getSubcommandName().equalsIgnoreCase(sub)) {
            context.setUsage(context.getLabel() + " action add ");
            command.run(context);
            return;
        }
        context.send(Component.text("Invalid action type, available action types:\n" +
                commands.stream().map(InteractionCommandHandler::getSubcommandName).collect(Collectors.joining(", ")), NamedTextColor.RED));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        List<InteractionCommandHandler> commands = actionRegistry.getCommands();
        if (context.argSize() == 2) return context.suggestStream(commands.stream().map(InteractionCommandHandler::getSubcommandName));
        context.popString();
        String sub = context.popString();
        for (InteractionCommandHandler command : commands) if (command.getSubcommandName().equalsIgnoreCase(sub)) return command.suggest(context);
        return Collections.emptyList();
    }
}
