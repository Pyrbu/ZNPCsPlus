package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.util.FrogVariant;

public class V1_19MetadataFactory extends V1_17MetadataFactory {
    @Override
    public EntityData frogVariant(int variant) {
        return newEntityData(17, EntityDataTypes.FROG_VARIANT, variant);
    }
}
