package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

import java.util.Deque;

public class NpcEntryParser extends ParserType<NpcEntryImpl> {
    private final NpcRegistryImpl npcRegistry;

    public NpcEntryParser(NpcRegistryImpl npcRegistry, Message<CommandContext> message) {
        super(message);
        this.npcRegistry = npcRegistry;
    }

    @Override
    public NpcEntryImpl parse(Deque<String> deque) throws CommandExecutionException {
        NpcEntryImpl entry = npcRegistry.getById(deque.pop());
        if (entry == null || !entry.isAllowCommandModification()) throw new CommandExecutionException();
        return entry;
    }
}
