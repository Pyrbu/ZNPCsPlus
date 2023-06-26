package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class V1_8MetadataFactory implements MetadataFactory {
    @Override
    public EntityData skinLayers(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat) {
        return createSkinLayers(12, cape, jacket, leftSleeve, rightSleeve, leftLeg, rightLeg, hat);
    }

    @Override
    public EntityData effects(boolean onFire, boolean glowing, boolean invisible, boolean usingElytra) {
        return newEntityData(0, EntityDataTypes.BYTE, (byte) ((onFire ? 0x01 : 0) | (invisible ? 0x20 : 0)));
    }

    @Override
    public EntityData name(Component name) {
        return newEntityData(2, EntityDataTypes.STRING, AdventureSerializer.getLegacyGsonSerializer().serialize(name));
    }

    @Override
    public EntityData nameShown() {
        return newEntityData(3, EntityDataTypes.BYTE, (byte) 1);
    }

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
        return newEntityData(0, EntityDataTypes.BYTE, (byte) (enabled ? 0x10 : 0));
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
    public EntityData silent(boolean enabled) {
        return newEntityData(4, EntityDataTypes.BYTE, (byte) (enabled ? 1 : 0));
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
}
