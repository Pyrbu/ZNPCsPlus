package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;

public class V1_10MetadataFactory extends V1_9MetadataFactory {
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
}
