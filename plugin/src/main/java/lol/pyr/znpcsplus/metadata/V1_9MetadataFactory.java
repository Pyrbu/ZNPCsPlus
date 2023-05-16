package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import lol.pyr.znpcsplus.util.list.ListUtil;
import net.kyori.adventure.text.Component;

import java.util.Collection;

public class V1_9MetadataFactory extends V1_8MetadataFactory {
    @Override
    public EntityData skinLayers(boolean enabled) {
        return createSkinLayers(13, enabled);
    }

    @Override
    public EntityData effects(boolean onFire, boolean glowing, boolean invisible) {
        return new EntityData(0, EntityDataTypes.BYTE, (byte) ((onFire ? 0x01 : 0) | (invisible ? 0x20 : 0) | (glowing ? 0x40 : 0)));
    }

    @Override
    public Collection<EntityData> name(Component name) {
        return ListUtil.immutableList(
                new EntityData(2, EntityDataTypes.STRING, AdventureSerializer.getGsonSerializer().serialize(name)),
                new EntityData(3, EntityDataTypes.BOOLEAN, true)
        );
    }

    @Override
    public EntityData silent(boolean enabled) {
        return new EntityData(4, EntityDataTypes.BOOLEAN, enabled);
    }
}
