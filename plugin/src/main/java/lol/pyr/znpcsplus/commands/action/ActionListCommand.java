package lol.pyr.znpcsplus.commands.action;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;

import java.util.List;

public class ActionListCommand implements CommandHandler {
    @Override
    public void run(CommandContext commandContext) throws CommandExecutionException {

    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        return CommandHandler.super.suggest(context);
    }
}
