package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;

public class V1_14Factory extends V1_9Factory {
    @Override
    public EntityData skinLayers() {
        return createSkinLayers(15);
    }
}
