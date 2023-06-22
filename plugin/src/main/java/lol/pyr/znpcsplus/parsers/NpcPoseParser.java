package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.util.NpcPose;

import java.util.Deque;

public class NpcPoseParser extends ParserType<NpcPose> {
    public NpcPoseParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public NpcPose parse(Deque<String> deque) throws CommandExecutionException {
        try {
            return NpcPose.valueOf(deque.pop().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommandExecutionException();
        }
    }
}
