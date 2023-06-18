package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.Optional;

public class V1_13MetadataFactory extends V1_10MetadataFactory {
    @Override
    public Collection<EntityData> name(Component name) {
        return list(
                newEntityData(2, EntityDataTypes.OPTIONAL_COMPONENT, Optional.of(AdventureSerializer.getGsonSerializer().serialize(name))),
                newEntityData(3, EntityDataTypes.BOOLEAN, true)
        );
    }
}
