package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.NpcPose;
import org.bukkit.entity.Player;

import java.util.Map;

public class NpcPoseProperty extends EntityPropertyImpl<NpcPose> {

    public NpcPoseProperty() {
        super("pose", NpcPose.STANDING, NpcPose.class);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        properties.put(6, newEntityData(6, EntityDataTypes.ENTITY_POSE, EntityPose.valueOf(entity.getProperty(this).name())));
    }
}
