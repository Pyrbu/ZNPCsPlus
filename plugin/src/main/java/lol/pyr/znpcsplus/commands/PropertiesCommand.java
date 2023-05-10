package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;

import java.util.Collections;
import java.util.List;

public class PropertiesCommand implements CommandHandler {
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.send("Not implemented yet!");
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        return Collections.emptyList();
    }
}
