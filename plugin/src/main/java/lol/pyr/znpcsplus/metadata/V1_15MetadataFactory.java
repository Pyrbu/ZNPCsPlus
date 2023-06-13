package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;

public class V1_15MetadataFactory extends V1_14MetadataFactory {
    @Override
    public EntityData cape(boolean enabled) {
        return createCape(16, enabled);
    }
}
