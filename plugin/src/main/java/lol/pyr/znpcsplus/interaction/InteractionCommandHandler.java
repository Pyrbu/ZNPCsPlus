package lol.pyr.znpcsplus.interaction;

import lol.pyr.director.adventure.command.CommandHandler;

public interface InteractionCommandHandler extends CommandHandler {
    String getSubcommandName();
}
