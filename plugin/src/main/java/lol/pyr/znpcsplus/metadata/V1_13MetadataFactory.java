package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import net.kyori.adventure.text.Component;

import java.util.Optional;

@Deprecated
public class V1_13MetadataFactory extends V1_12MetadataFactory {
    @Override
    public EntityData name(Component name) {
        return newEntityData(2, EntityDataTypes.OPTIONAL_COMPONENT, Optional.of(AdventureSerializer.getGsonSerializer().serialize(name)));
    }

    @Override
    public EntityData usingItem(boolean usingItem, boolean offHand, boolean riptide) {
        return newEntityData(6, EntityDataTypes.BYTE, (byte) ((usingItem ? 0x01 : 0) | (offHand ? 0x02 : 0) | (riptide ? 0x04 : 0)));
    }
}
