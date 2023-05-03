package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import lol.pyr.znpcsplus.util.list.ListUtil;
import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.Optional;

public class V1_13Factory extends V1_9Factory {
    @Override
    public Collection<EntityData> name(Component name) {
        return ListUtil.immutableList(
                new EntityData(2, EntityDataTypes.OPTIONAL_COMPONENT, Optional.of(AdventureSerializer.getGsonSerializer().serialize(name))),
                new EntityData(3, EntityDataTypes.BOOLEAN, true)
        );
    }
}
