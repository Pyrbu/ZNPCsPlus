package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;

public class V1_9Factory extends V1_8Factory {
    @Override
    public EntityData skinLayers() {
        return createSkinLayers(13);
    }

    @Override
    public EntityData effects(boolean onFire, boolean glowing) {
        return new EntityData(0, EntityDataTypes.BYTE, (byte) ((onFire ? 0x01 : 0) | (glowing ? 0x40 : 0)));
    }
}
