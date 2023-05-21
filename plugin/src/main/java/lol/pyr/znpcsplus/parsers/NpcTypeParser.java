package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;

import java.util.Deque;

public class NpcTypeParser extends ParserType<NpcTypeImpl> {
    private final NpcTypeRegistryImpl typeRegistry;

    public NpcTypeParser(Message<CommandContext> message, NpcTypeRegistryImpl typeRegistry) {
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
