package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import lol.pyr.znpcsplus.util.CreeperState;
import net.kyori.adventure.text.Component;

@Deprecated
public class V1_9MetadataFactory extends V1_8MetadataFactory {
    @Override
    public EntityData skinLayers(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat) {
        return createSkinLayers(12, cape, jacket, leftSleeve, rightSleeve, leftLeg, rightLeg, hat);
    }

    @Override
    public EntityData effects(boolean onFire, boolean glowing, boolean invisible, boolean usingElytra, boolean usingItemLegacy) {
        return newEntityData(0, EntityDataTypes.BYTE, (byte) (
                (onFire ? 0x01 : 0) |
                (usingItemLegacy ? 0x10 : 0) |
                (invisible ? 0x20 : 0) |
                (glowing ? 0x40 : 0) |
                (usingElytra ? 0x80 : 0)
        ));
    }

    @Override
    public EntityData potionAmbient(boolean ambient) {
        return newEntityData(8, EntityDataTypes.BOOLEAN, ambient);
    }

    @Override
    public EntityData batHanging(boolean hanging) {
        return newEntityData(11, EntityDataTypes.BYTE, (byte) (hanging ? 0x01 : 0));
    }

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (onFire ? 1 : 0));
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(11, EntityDataTypes.INT, state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(12, EntityDataTypes.BOOLEAN, charged);
    }

    @Override
    public EntityData name(Component name) {
        return newEntityData(2, EntityDataTypes.STRING, AdventureSerializer.getGsonSerializer().serialize(name));
    }

    @Override
    public EntityData nameShown() {
        return newEntityData(3, EntityDataTypes.BOOLEAN, true);
    }

    @Override
    public EntityData silent(boolean enabled) {
        return newEntityData(4, EntityDataTypes.BOOLEAN, enabled);
    }

    @Override
    public EntityData ghastAttacking(boolean attacking) {
        return newEntityData(11, EntityDataTypes.BOOLEAN, attacking);
    }

    @Override
    public EntityData villagerData(int type, int profession, int level) {
        return newEntityData(12, EntityDataTypes.INT, profession);
    }
}
