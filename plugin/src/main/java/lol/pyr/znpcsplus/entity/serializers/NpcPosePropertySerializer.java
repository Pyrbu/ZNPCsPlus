package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.util.NpcPose;

public class NpcPosePropertySerializer implements PropertySerializer<NpcPose> {
    @Override
    public String serialize(NpcPose property) {
        return property.name();
    }

    @Override
    public NpcPose deserialize(String property) {
        return NpcPose.valueOf(property.toUpperCase());
    }

    @Override
    public Class<NpcPose> getTypeClass() {
        return NpcPose.class;
    }
}
