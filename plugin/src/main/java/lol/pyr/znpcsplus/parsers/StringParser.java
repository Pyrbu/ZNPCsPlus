package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;

import java.util.Deque;

public class StringParser extends ParserType<String> {
    public StringParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public String parse(Deque<String> deque) throws CommandExecutionException {
        return String.join(" ", deque);
    }
}
