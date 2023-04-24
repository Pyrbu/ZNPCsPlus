package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;

public class V1_17Factory extends V1_16Factory {
    @Override
    public EntityData skinLayers() {
        return createSkinLayers(17);
    }
}
