package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.api.interaction.InteractionType;

import java.util.Deque;

public class InteractionTypeParser extends ParserType<InteractionType> {
    public InteractionTypeParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public InteractionType parse(Deque<String> deque) throws CommandExecutionException {
        try {
            return InteractionType.valueOf(deque.pop().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            throw new CommandExecutionException();
        }
    }
}
