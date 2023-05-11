package lol.pyr.znpcsplus.commands.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;

import java.util.Deque;

@SuppressWarnings("rawtypes")
public class EntityPropertyParser extends ParserType<EntityPropertyImpl/*<?>*/> {
    public EntityPropertyParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public EntityPropertyImpl<?> parse(Deque<String> deque) throws CommandExecutionException {
        EntityPropertyImpl<?> property = EntityPropertyImpl.getByName(deque.pop());
        if (property == null) throw new CommandExecutionException();
        return property;
    }
}
