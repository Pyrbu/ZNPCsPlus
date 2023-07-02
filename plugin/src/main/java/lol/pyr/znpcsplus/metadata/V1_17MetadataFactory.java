package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.util.CatVariant;
import lol.pyr.znpcsplus.util.CreeperState;
import lol.pyr.znpcsplus.util.ParrotVariant;
import lol.pyr.znpcsplus.util.Vector3f;
import org.bukkit.DyeColor;

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
    public EntityData shoulderEntityLeft(ParrotVariant variant) {
        return createShoulderEntityLeft(19, variant);
    }

    @Override
    public EntityData shoulderEntityRight(ParrotVariant variant) {
        return createShoulderEntityRight(20, variant);
    }

    @Override
    public EntityData armorStandProperties(boolean small, boolean arms, boolean noBasePlate) {
        return newEntityData(15, EntityDataTypes.BYTE, (byte) ((small ? 0x01 : 0) | (arms ? 0x04 : 0) | (noBasePlate ? 0x08 : 0)));
    }

    @Override
    public EntityData armorStandHeadRotation(Vector3f headRotation) {
        return createRotations(16, headRotation);
    }

    @Override
    public EntityData armorStandBodyRotation(Vector3f bodyRotation) {
        return createRotations(17, bodyRotation);
    }

    @Override
    public EntityData armorStandLeftArmRotation(Vector3f leftArmRotation) {
        return createRotations(18, leftArmRotation);
    }

    @Override
    public EntityData armorStandRightArmRotation(Vector3f rightArmRotation) {
        return createRotations(19, rightArmRotation);
    }

    @Override
    public EntityData armorStandLeftLegRotation(Vector3f leftLegRotation) {
        return createRotations(20, leftLegRotation);
    }

    @Override
    public EntityData armorStandRightLegRotation(Vector3f rightLegRotation) {
        return createRotations(21, rightLegRotation);
    }

    @Override
    public EntityData axolotlVariant(int variant) {
        return newEntityData(17, EntityDataTypes.INT, variant);
    }

    @Override
    public EntityData playingDead(boolean playingDead) {
        return newEntityData(18, EntityDataTypes.BOOLEAN, playingDead);
    }

    @Override
    public EntityData batHanging(boolean hanging) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (hanging ? 0x01 : 0));
    }

    @Override
    public EntityData beeAngry(boolean angry) {
        return newEntityData(18, EntityDataTypes.INT, angry ? 1 : 0);
    }

    @Override
    public EntityData beeHasNectar(boolean hasNectar) {
        return newEntityData(17, EntityDataTypes.BYTE, (byte) (hasNectar ? 0x08 : 0));
    }

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (onFire ? 0x01 : 0));
    }

    @Override
    public EntityData catVariant(CatVariant variant) {
        return newEntityData(19, EntityDataTypes.CAT_VARIANT, variant.getId());
    }

    @Override
    public EntityData catLying(boolean lying) {
        return newEntityData(20, EntityDataTypes.BOOLEAN, lying);
    }

    @Override
    public EntityData catTamed(boolean tamed) {
        return newEntityData(17, EntityDataTypes.BYTE, (byte) (tamed ? 0x04 : 0));
    }

    @Override
    public EntityData catCollarColor(DyeColor collarColor) {
        return newEntityData(22, EntityDataTypes.INT, collarColor.ordinal());
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(16, EntityDataTypes.INT, state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(17, EntityDataTypes.BOOLEAN, charged);
    }
}
