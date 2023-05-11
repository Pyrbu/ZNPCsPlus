package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;

public class V1_14Factory extends V1_13Factory {
    @Override
    public EntityData skinLayers(boolean enabled) {
        return createSkinLayers(15, enabled);
    }
}
