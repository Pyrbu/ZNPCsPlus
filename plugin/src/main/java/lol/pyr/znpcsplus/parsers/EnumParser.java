package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;

import java.util.Deque;

public class EnumParser<T extends Enum<T>> extends ParserType<T> {

    private final Class<T> enumClass;

    public EnumParser(Class<T> enumClass, Message<CommandContext> message) {
        super(message);
        this.enumClass = enumClass;
    }

    @Override
    public T parse(Deque<String> deque) throws CommandExecutionException {
        try {
            return Enum.valueOf(enumClass, deque.pop().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommandExecutionException();
        }
    }
}
