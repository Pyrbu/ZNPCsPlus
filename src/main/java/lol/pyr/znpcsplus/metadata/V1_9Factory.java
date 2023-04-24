package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;

public class V1_9Factory extends V1_8Factory {
    @Override
    public EntityData skinLayers() {
        return createSkinLayers(13);
    }
}
