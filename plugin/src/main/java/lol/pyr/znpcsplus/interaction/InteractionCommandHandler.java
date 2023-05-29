package lol.pyr.znpcsplus.interaction;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcImpl;

public interface InteractionCommandHandler extends CommandHandler {
    String getSubcommandName();

    InteractionAction parse(CommandContext context, NpcImpl npc) throws CommandExecutionException;
}
