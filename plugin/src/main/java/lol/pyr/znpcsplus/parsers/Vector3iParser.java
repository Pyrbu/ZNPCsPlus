package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.util.Vector3i;

import java.util.Deque;

public class Vector3iParser extends ParserType<Vector3i> {
    public Vector3iParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public Vector3i parse(Deque<String> deque) throws CommandExecutionException {
        if (deque.size() == 0) {
            return null;
        }
        try {
            return new Vector3i(
                    Integer.parseInt(deque.pop()),
                    Integer.parseInt(deque.pop()),
                    Integer.parseInt(deque.pop()));
        } catch (NumberFormatException e) {
            throw new CommandExecutionException();
        }
    }
}
