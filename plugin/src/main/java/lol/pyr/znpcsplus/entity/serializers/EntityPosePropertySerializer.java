package lol.pyr.znpcsplus.entity.serializers;

import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import lol.pyr.znpcsplus.entity.PropertySerializer;

public class EntityPosePropertySerializer implements PropertySerializer<EntityPose> {
    @Override
    public String serialize(EntityPose property) {
        return property.name();
    }

    @Override
    public EntityPose deserialize(String property) {
        return EntityPose.valueOf(property.toUpperCase());
    }

    @Override
    public Class<EntityPose> getTypeClass() {
        return EntityPose.class;
    }
}
