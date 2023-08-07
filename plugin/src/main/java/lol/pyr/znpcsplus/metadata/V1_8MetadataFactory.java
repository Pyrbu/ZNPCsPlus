package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import lol.pyr.znpcsplus.util.*;
import org.bukkit.DyeColor;

@Deprecated
public class V1_8MetadataFactory implements MetadataFactory {

    @Override
    public EntityData noGravity() {
        throw new UnsupportedOperationException("The gravity entity data isn't supported on this version");
    }

    @Override
    public EntityData pose(EntityPose pose) {
        throw new UnsupportedOperationException("The pose entity data isn't supported on this version");
    }

    @Override
    public EntityData shaking(boolean enabled) {
        throw new UnsupportedOperationException("The shaking entity data isn't supported on this version");
    }

    @Override
    public EntityData usingItem(boolean enabled, boolean offHand, boolean riptide) {
        throw new UnsupportedOperationException("The standalone using item data isn't supported on this version");
    }

    @Override
    public EntityData potionColor(int color) {
        return newEntityData(7, EntityDataTypes.INT, color);
    }

    @Override
    public EntityData potionAmbient(boolean ambient) {
        return newEntityData(8, EntityDataTypes.BYTE, (byte) (ambient ? 1 : 0));
    }

    @Override
    public EntityData shoulderEntityLeft(ParrotVariant variant) {
        throw new UnsupportedOperationException("The shoulder entity data isn't supported on this version");
    }

    @Override
    public EntityData shoulderEntityRight(ParrotVariant variant) {
        throw new UnsupportedOperationException("The shoulder entity data isn't supported on this version");
    }

    @Override
    public EntityData axolotlVariant(int variant) {
        throw new UnsupportedOperationException("The axolotl variant entity data isn't supported on this version");
    }

    @Override
    public EntityData playingDead(boolean playingDead) {
        throw new UnsupportedOperationException("The playing dead entity data isn't supported on this version");
    }

    @Override
    public EntityData batHanging(boolean hanging) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (hanging ? 1 : 0));
    }

    @Override
    public EntityData beeAngry(boolean angry) {
        throw new UnsupportedOperationException("The bee properties entity data isn't supported on this version");
    }

    @Override
    public EntityData beeHasNectar(boolean hasNectar) {
        throw new UnsupportedOperationException("The bee properties entity data isn't supported on this version");
    }

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (onFire ? 1 : 0));
    }

    @Override
    public EntityData catVariant(CatVariant variant) {
        throw new UnsupportedOperationException("The cat variant entity data isn't supported on this version");
    }

    @Override
    public EntityData catLying(boolean lying) {
        throw new UnsupportedOperationException("The cat lying entity data isn't supported on this version");
    }

    @Override
    public EntityData catTamed(boolean tamed) {
        throw new UnsupportedOperationException("The cat tamed entity data isn't supported on this version");
    }

    @Override
    public EntityData catCollarColor(DyeColor collarColor) {
        throw new UnsupportedOperationException("The cat collar color entity data isn't supported on this version");
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(17, EntityDataTypes.BYTE, (byte) (charged ? 1 : 0));
    }

    @Override
    public EntityData endermanHeldBlock(int carriedBlock) {
        throw new UnsupportedOperationException("The enderman carried block entity data isn't supported on this version");
    }

    @Override
    public EntityData endermanScreaming(boolean screaming) {
        throw new UnsupportedOperationException("The enderman screaming entity data isn't supported on this version");
    }

    @Override
    public EntityData endermanStaring(boolean staring) {
        return newEntityData(18, EntityDataTypes.BOOLEAN, staring);
    }

    @Override
    public EntityData evokerSpell(int spell) {
        throw new UnsupportedOperationException("The evoker spell entity data isn't supported on this version");
    }

    @Override
    public EntityData foxVariant(int variant) {
        throw new UnsupportedOperationException("The fox variant entity data isn't supported on this version");
    }

    @Override
    public EntityData foxProperties(boolean sitting, boolean crouching, boolean sleeping, boolean facePlanted) {
        throw new UnsupportedOperationException("The fox properties entity data isn't supported on this version");
    }

    @Override
    public EntityData frogVariant(int variant) {
        throw new UnsupportedOperationException("The frog variant entity data isn't supported on this version");
    }

    @Override
    public EntityData ghastAttacking(boolean attacking) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (attacking ? 1 : 0));
    }

    @Override
    public EntityData goatHasLeftHorn(boolean hasLeftHorn) {
        throw new UnsupportedOperationException("The goat horn entity data isn't supported on this version");
    }

    @Override
    public EntityData goatHasRightHorn(boolean hasRightHorn) {
        throw new UnsupportedOperationException("The goat horn entity data isn't supported on this version");
    }

    @Override
    public EntityData hoglinImmuneToZombification(boolean immuneToZombification) {
        throw new UnsupportedOperationException("The hoglin zombification entity data isn't supported on this version");
    }

    @Override
    public EntityData villagerData(int type, int profession, int level) {
        return newEntityData(16, EntityDataTypes.INT, profession);
    }

    protected EntityData createSkinLayers(int index, boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat) {
        return newEntityData(index, EntityDataTypes.BYTE, (byte) (
                (cape ? 0x01 : 0) |
                (jacket ? 0x02 : 0) |
                (leftSleeve ? 0x04 : 0) |
                (rightSleeve ? 0x08 : 0) |
                (leftLeg ? 0x10 : 0) |
                (rightLeg ? 0x20 : 0) |
                (hat ? 0x40 : 0))
        );
    }

    protected <T> EntityData newEntityData(int index, EntityDataType<T> type, T value) {
        return new EntityData(index, type, value);
    }

    protected EntityData createRotations(int index, Vector3f rotations) {
        return newEntityData(index, EntityDataTypes.ROTATION, new com.github.retrooper.packetevents.util.Vector3f(rotations.getX(), rotations.getY(), rotations.getZ()));
    }
}
