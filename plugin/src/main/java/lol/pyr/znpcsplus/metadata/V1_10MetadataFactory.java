package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.util.CreeperState;
import lol.pyr.znpcsplus.util.Vector3f;

@Deprecated
public class V1_10MetadataFactory extends V1_9MetadataFactory {
    @Override
    public EntityData skinLayers(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat) {
        return createSkinLayers(13, cape, jacket, leftSleeve, rightSleeve, leftLeg, rightLeg, hat);
    }

    @Override
    public EntityData noGravity() {
        return newEntityData(5, EntityDataTypes.BOOLEAN, true);
    }

    @Override
    public EntityData potionColor(int color) {
        return newEntityData(8, EntityDataTypes.INT, color);
    }

    @Override
    public EntityData potionAmbient(boolean ambient) {
        return newEntityData(9, EntityDataTypes.BOOLEAN, ambient);
    }

    @Override
    public EntityData armorStandProperties(boolean small, boolean arms, boolean noBasePlate) {
        return newEntityData(11, EntityDataTypes.BYTE, (byte) ((small ? 0x01 : 0) | (arms ? 0x04 : 0) | (noBasePlate ? 0x08 : 0)));
    }

    @Override
    public EntityData armorStandHeadRotation(Vector3f headRotation) {
        return createRotations(12, headRotation);
    }

    @Override
    public EntityData armorStandBodyRotation(Vector3f bodyRotation) {
        return createRotations(13, bodyRotation);
    }

    @Override
    public EntityData armorStandLeftArmRotation(Vector3f leftArmRotation) {
        return createRotations(14, leftArmRotation);
    }

    @Override
    public EntityData armorStandRightArmRotation(Vector3f rightArmRotation) {
        return createRotations(15, rightArmRotation);
    }

    @Override
    public EntityData armorStandLeftLegRotation(Vector3f leftLegRotation) {
        return createRotations(16, leftLegRotation);
    }

    @Override
    public EntityData armorStandRightLegRotation(Vector3f rightLegRotation) {
        return createRotations(17, rightLegRotation);
    }

    @Override
    public EntityData batHanging(boolean hanging) {
        return newEntityData(12, EntityDataTypes.BYTE, (byte) (hanging ? 0x01 : 0));
    }

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(12, EntityDataTypes.BYTE, (byte) (onFire ? 0x01 : 0));
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(12, EntityDataTypes.INT, state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(13, EntityDataTypes.BOOLEAN, charged);
    }

    @Override
    public EntityData ghastAttacking(boolean attacking) {
        return newEntityData(12, EntityDataTypes.BOOLEAN, attacking);
    }

    @Override
    public EntityData villagerData(int type, int profession, int level) {
        return newEntityData(13, EntityDataTypes.INT, profession);
    }
}
