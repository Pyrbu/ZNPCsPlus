package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.util.Vector3f;

public class V1_17MetadataFactory extends V1_16MetadataFactory {
    @Override
    public EntityData skinLayers(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat) {
        return createSkinLayers(17, cape, jacket, leftSleeve, rightSleeve, leftLeg, rightLeg, hat);
    }

    @Override
    public EntityData effects(boolean onFire, boolean glowing, boolean invisible, boolean usingElytra, boolean usingItemLegacy) {
        return newEntityData(0, EntityDataTypes.BYTE, (byte) ((onFire ? 0x01 : 0) | (invisible ? 0x20 : 0) | (glowing ? 0x40 : 0) | (usingElytra ? 0x80 : 0)));
    }

    @Override
    public EntityData shaking(boolean enabled) {
        return newEntityData(7, EntityDataTypes.INT, enabled ? 140 : 0);
    }

    @Override
    public EntityData usingItem(boolean usingItem, boolean offHand, boolean riptide) {
        return newEntityData(8, EntityDataTypes.BYTE, (byte) ((usingItem ? 0x01 : 0) | (offHand ? 0x02 : 0) | (riptide ? 0x04 : 0)));
    }

    @Override
    public EntityData potionColor(int color) {
        return newEntityData(10, EntityDataTypes.INT, color);
    }

    @Override
    public EntityData potionAmbient(boolean ambient) {
        return newEntityData(11, EntityDataTypes.BOOLEAN, ambient);
    }

    @Override
    public EntityData armorStandProperties(boolean small, boolean arms, boolean noBasePlate) {
        return newEntityData(15, EntityDataTypes.BYTE, (byte) ((small ? 0x01 : 0) | (arms ? 0x04 : 0) | (noBasePlate ? 0x08 : 0)));
    }

    @Override
    public EntityData armorStandHeadRotation(Vector3f headRotation) {
        return newEntityData(16, EntityDataTypes.ROTATION, new com.github.retrooper.packetevents.util.Vector3f(headRotation.getX(), headRotation.getY(), headRotation.getZ()));
    }

    @Override
    public EntityData armorStandBodyRotation(Vector3f bodyRotation) {
        return newEntityData(17, EntityDataTypes.ROTATION, new com.github.retrooper.packetevents.util.Vector3f(bodyRotation.getX(), bodyRotation.getY(), bodyRotation.getZ()));
    }

    @Override
    public EntityData armorStandLeftArmRotation(Vector3f leftArmRotation) {
        return newEntityData(18, EntityDataTypes.ROTATION, new com.github.retrooper.packetevents.util.Vector3f(leftArmRotation.getX(), leftArmRotation.getY(), leftArmRotation.getZ()));
    }

    @Override
    public EntityData armorStandRightArmRotation(Vector3f rightArmRotation) {
        return newEntityData(19, EntityDataTypes.ROTATION, new com.github.retrooper.packetevents.util.Vector3f(rightArmRotation.getX(), rightArmRotation.getY(), rightArmRotation.getZ()));
    }

    @Override
    public EntityData armorStandLeftLegRotation(Vector3f leftLegRotation) {
        return newEntityData(20, EntityDataTypes.ROTATION, new com.github.retrooper.packetevents.util.Vector3f(leftLegRotation.getX(), leftLegRotation.getY(), leftLegRotation.getZ()));
    }

    @Override
    public EntityData armorStandRightLegRotation(Vector3f rightLegRotation) {
        return newEntityData(21, EntityDataTypes.ROTATION, new com.github.retrooper.packetevents.util.Vector3f(rightLegRotation.getX(), rightLegRotation.getY(), rightLegRotation.getZ()));
    }


}
