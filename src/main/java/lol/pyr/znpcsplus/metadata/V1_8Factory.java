package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.List;

public class V1_8Factory implements MetadataFactory {
    @Override
    public EntityData skinLayers() {
        return createSkinLayers(12);
    }

    @Override
    public EntityData effects(boolean onFire, boolean glowing, boolean invisible) {
        return new EntityData(0, EntityDataTypes.BYTE, (onFire ? 0x01 : 0) | (invisible ? 0x20 : 0));
    }

    @Override
    public Collection<EntityData> name(Component name) {
        return List.of(
                new EntityData(2, EntityDataTypes.STRING, AdventureSerializer.getGsonSerializer().serialize(name)),
                new EntityData(3, EntityDataTypes.BYTE, 1)
        );
    }

    @Override
    public EntityData silent() {
        return new EntityData(4, EntityDataTypes.BYTE, 1);
    }

    protected EntityData createSkinLayers(int index) {
        return new EntityData(index, EntityDataTypes.BYTE, Byte.MAX_VALUE);
    }
}
