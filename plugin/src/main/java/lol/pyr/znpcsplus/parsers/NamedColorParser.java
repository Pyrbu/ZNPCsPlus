package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.util.NamedColor;
import java.util.Deque;

public class NamedColorParser extends ParserType<NamedColor> {
    public NamedColorParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public NamedColor parse(Deque<String> deque) throws CommandExecutionException {
        try {
            return NamedColor.valueOf(deque.pop());
        } catch (IllegalArgumentException exception) {
            throw new CommandExecutionException();
        }
    }
}