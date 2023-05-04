package lol.pyr.znpcsplus.command;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

public class CreateCommand implements CommandHandler {
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        String id = context.popString();
        NpcRegistryImpl.get().get(id);
    }
}
