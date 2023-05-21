package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;

import java.util.Deque;

@SuppressWarnings("rawtypes")
public class EntityPropertyParser extends ParserType<EntityPropertyImpl/*<?>*/> {
    private final EntityPropertyRegistryImpl propertyRegistry;

    public EntityPropertyParser(Message<CommandContext> message, EntityPropertyRegistryImpl propertyRegistry) {
        super(message);
        this.propertyRegistry = propertyRegistry;
    }

    @Override
    public EntityPropertyImpl<?> parse(Deque<String> deque) throws CommandExecutionException {
        EntityPropertyImpl<?> property = propertyRegistry.getByName(deque.pop());
        if (property == null) throw new CommandExecutionException();
        return property;
    }
}
