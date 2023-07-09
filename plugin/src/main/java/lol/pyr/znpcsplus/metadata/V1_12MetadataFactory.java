package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import lol.pyr.znpcsplus.entity.ParrotNBTCompound;
import lol.pyr.znpcsplus.util.ParrotVariant;

@Deprecated
public class V1_12MetadataFactory extends V1_11MetadataFactory {
    @Override
    public EntityData shoulderEntityLeft(ParrotVariant variant) {
        return createShoulderEntityLeft(15, variant);
    }

    public EntityData createShoulderEntityLeft(int index, ParrotVariant variant) {
        return newEntityData(index, EntityDataTypes.NBT, variant == ParrotVariant.NONE ? new NBTCompound() : new ParrotNBTCompound(variant).getTag());
    }

    @Override
    public EntityData shoulderEntityRight(ParrotVariant variant) {
        return createShoulderEntityRight(16, variant);
    }

    public EntityData createShoulderEntityRight(int index, ParrotVariant variant) {
        return newEntityData(index, EntityDataTypes.NBT, variant == ParrotVariant.NONE ? new NBTCompound() : new ParrotNBTCompound(variant).getTag());
    }

    @Override
    public EntityData evokerSpell(int spell) {
        return newEntityData(13, EntityDataTypes.BYTE, (byte) spell);
    }
}
