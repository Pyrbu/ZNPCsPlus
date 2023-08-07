package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import lol.pyr.znpcsplus.util.CreeperState;
import lol.pyr.znpcsplus.util.ParrotVariant;

@Deprecated
public class V1_14MetadataFactory extends V1_13MetadataFactory {

    @Override
    public EntityData pose(EntityPose pose) {
        return newEntityData(6, EntityDataTypes.ENTITY_POSE, pose);
    }

    @Override
    public EntityData usingItem(boolean usingItem, boolean offHand, boolean riptide) {
        return newEntityData(7, EntityDataTypes.BYTE, (byte) ((usingItem ? 0x01 : 0) | (offHand ? 0x02 : 0) | (riptide ? 0x04 : 0)));
    }

    @Override
    public EntityData shoulderEntityLeft(ParrotVariant variant) {
        return createShoulderEntityLeft(17, variant);
    }

    @Override
    public EntityData shoulderEntityRight(ParrotVariant variant) {
        return createShoulderEntityRight(18, variant);
    }

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(14, EntityDataTypes.BYTE, (byte) (onFire ? 0x01 : 0));
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(14, EntityDataTypes.INT, state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(15, EntityDataTypes.BOOLEAN, charged);
    }

    @Override
    public EntityData evokerSpell(int spell) {
        return newEntityData(15, EntityDataTypes.BYTE, (byte) spell);
    }

    @Override
    public EntityData villagerData(int type, int profession, int level) {
        return newEntityData(16, EntityDataTypes.VILLAGER_DATA, new VillagerData(type, profession, level));
    }
}
