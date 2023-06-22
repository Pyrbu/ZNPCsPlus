package lol.pyr.znpcsplus.parsers;

import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;

import java.util.Deque;

public class EntityPoseParser extends ParserType<EntityPose> {

    public EntityPoseParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public EntityPose parse(Deque<String> deque) throws CommandExecutionException {
        try {
            return EntityPose.valueOf(deque.pop().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommandExecutionException();
        }
    }
}
