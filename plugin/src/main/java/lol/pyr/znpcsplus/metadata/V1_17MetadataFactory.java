package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;

public class V1_17MetadataFactory extends V1_16MetadataFactory {
    @Override
    public EntityData skinLayers(boolean enabled) {
        return createSkinLayers(17, enabled);
    }
}
