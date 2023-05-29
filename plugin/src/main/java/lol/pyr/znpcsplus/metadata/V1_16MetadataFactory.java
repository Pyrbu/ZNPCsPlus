package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;

public class V1_16MetadataFactory extends V1_15MetadataFactory {
    @Override
    public EntityData skinLayers(boolean enabled) {
        return createSkinLayers(16, enabled);
    }
}
