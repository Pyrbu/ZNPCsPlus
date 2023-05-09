package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;

import java.util.List;

public class ActionCommand implements CommandHandler {

    @Override
    public void run(CommandContext commandContext) throws CommandExecutionException {
        commandContext.getSender().sendMessage("Not implemented yet.");
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        return CommandHandler.super.suggest(context);
    }
}
