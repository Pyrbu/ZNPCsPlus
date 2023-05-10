package lol.pyr.znpcsplus.commands.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;

import java.util.Deque;

public class NpcTypeParser extends ParserType<NpcTypeImpl> {
    public NpcTypeParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public NpcTypeImpl parse(Deque<String> deque) throws CommandExecutionException {
        NpcTypeImpl type = NpcTypeImpl.byName(deque.pop());
        if (type == null) throw new CommandExecutionException();
        return type;
    }
}
