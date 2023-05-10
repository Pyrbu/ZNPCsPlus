package lol.pyr.znpcsplus.commands.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

import java.util.Deque;

public class NpcEntryParser extends ParserType<NpcEntryImpl> {
    public NpcEntryParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public NpcEntryImpl parse(Deque<String> deque) throws CommandExecutionException {
        NpcEntryImpl entry = NpcRegistryImpl.get().get(deque.pop());
        if (entry == null || !entry.isAllowCommandModification()) throw new CommandExecutionException();
        return entry;
    }
}
