package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.util.CatVariant;
import lol.pyr.znpcsplus.util.CreeperState;
import lol.pyr.znpcsplus.util.ParrotVariant;
import lol.pyr.znpcsplus.util.Vector3f;
import org.bukkit.DyeColor;

public class V1_15MetadataFactory extends V1_14MetadataFactory {
    @Override
    public EntityData shoulderEntityLeft(ParrotVariant variant) {
            return createShoulderEntityLeft(18, variant);
    }

    @Override
    public EntityData shoulderEntityRight(ParrotVariant variant) {
        return createShoulderEntityRight(19, variant);
    }

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

    @Override
    public EntityData batHanging(boolean hanging) {
        return newEntityData(15, EntityDataTypes.BYTE, (byte) (hanging ? 0x01 : 0));
    }

    @Override
    public EntityData beeAngry(boolean angry) {
        return newEntityData(17, EntityDataTypes.INT, angry ? 1 : 0);
    }

    @Override
    public EntityData beeHasNectar(boolean hasNectar) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (hasNectar ? 0x08 : 0));
    }

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(15, EntityDataTypes.BYTE, (byte) (onFire ? 0x01 : 0));
    }

    @Override
    public EntityData catVariant(CatVariant variant) {
        return newEntityData(18, EntityDataTypes.CAT_VARIANT, variant.getId());
    }

    @Override
    public EntityData catLying(boolean lying) {
        return newEntityData(19, EntityDataTypes.BOOLEAN, lying);
    }

    @Override
    public EntityData catCollarColor(DyeColor collarColor) {
        return newEntityData(21, EntityDataTypes.INT, collarColor.ordinal());
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(15, EntityDataTypes.INT, state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(16, EntityDataTypes.BOOLEAN, charged);
    }

    @Override
    public EntityData evokerSpell(int spell) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) spell);
    }
}
