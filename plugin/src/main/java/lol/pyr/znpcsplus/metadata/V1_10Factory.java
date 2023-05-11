package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;

public class V1_10Factory extends V1_9Factory {
    @Override
    public EntityData noGravity() {
        return new EntityData(5, EntityDataTypes.BOOLEAN, true);
    }
}
