package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import lol.pyr.znpcsplus.util.CatVariant;
import lol.pyr.znpcsplus.util.CreeperState;
import lol.pyr.znpcsplus.util.ParrotVariant;
import org.bukkit.DyeColor;

@Deprecated
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

    @Override
    public EntityData foxVariant(int variant) {
        return newEntityData(16, EntityDataTypes.INT, variant);
    }

    @Override
    public EntityData foxProperties(boolean sitting, boolean crouching, boolean sleeping, boolean facePlanted) {
        return newEntityData(17, EntityDataTypes.BYTE, (byte) ((sitting ? 0x01 : 0) | (crouching ? 0x04 : 0) | (sleeping ? 0x20 : 0) | (facePlanted ? 0x40 : 0)));
    }

    @Override
    public EntityData villagerData(int type, int profession, int level) {
        return newEntityData(17, EntityDataTypes.VILLAGER_DATA, new VillagerData(type, profession, level));
    }
}
