package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.util.Vector3f;

public class V1_15MetadataFactory extends V1_14MetadataFactory {
    @Override
    public EntityData armorStandProperties(boolean small, boolean arms, boolean noBasePlate) {
        return newEntityData(14, EntityDataTypes.BYTE, (byte) ((small ? 0x01 : 0) | (arms ? 0x04 : 0) | (!noBasePlate ? 0x08 : 0)));
    }

    @Override
    public EntityData armorStandHeadRotation(Vector3f headRotation) {
        return createRotations(15, headRotation);
    }

    @Override
    public EntityData armorStandBodyRotation(Vector3f bodyRotation) {
        return createRotations(16, bodyRotation);
    }

    @Override
    public EntityData armorStandLeftArmRotation(Vector3f leftArmRotation) {
        return createRotations(17, leftArmRotation);
    }

    @Override
    public EntityData armorStandRightArmRotation(Vector3f rightArmRotation) {
        return createRotations(18, rightArmRotation);
    }

    @Override
    public EntityData armorStandLeftLegRotation(Vector3f leftLegRotation) {
        return createRotations(19, leftLegRotation);
    }

    @Override
    public EntityData armorStandRightLegRotation(Vector3f rightLegRotation) {
        return createRotations(20, rightLegRotation);
    }
}
