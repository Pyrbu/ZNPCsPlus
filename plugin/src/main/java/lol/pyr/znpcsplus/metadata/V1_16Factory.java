package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;

public class V1_16Factory extends V1_14Factory {
    @Override
    public EntityData skinLayers(boolean enabled) {
        return createSkinLayers(16, enabled);
    }
}
