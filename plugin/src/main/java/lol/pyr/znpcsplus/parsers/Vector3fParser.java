package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.util.Vector3f;

import java.util.Deque;

public class Vector3fParser extends ParserType<Vector3f> {
    public Vector3fParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public Vector3f parse(Deque<String> deque) throws CommandExecutionException {
        try {
            return new Vector3f(
                    Float.parseFloat(deque.pop()),
                    Float.parseFloat(deque.pop()),
                    Float.parseFloat(deque.pop()));
        } catch (NumberFormatException e) {
            throw new CommandExecutionException();
        }
    }
}
