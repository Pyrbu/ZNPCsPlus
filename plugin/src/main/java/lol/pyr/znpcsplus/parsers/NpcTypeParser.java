package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistry;

import java.util.Deque;

public class NpcTypeParser extends ParserType<NpcTypeImpl> {
    private final NpcTypeRegistry typeRegistry;

    public NpcTypeParser(Message<CommandContext> message, NpcTypeRegistry typeRegistry) {
        super(message);
        this.typeRegistry = typeRegistry;
    }

    @Override
    public NpcTypeImpl parse(Deque<String> deque) throws CommandExecutionException {
        NpcTypeImpl type = typeRegistry.getByName(deque.pop());
        if (type == null) throw new CommandExecutionException();
        return type;
    }
}
