package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.RabbitType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class RabbitTypeProperty extends EntityPropertyImpl<RabbitType> {
    private final int index;
    private final boolean legacyBooleans;
    private final Object serialized;
    private final EntityDataType<?> type;

    public RabbitTypeProperty(int index, boolean legacyBooleans, boolean legacyNames, boolean optional) {
        super("rabbit_type", RabbitType.BROWN, RabbitType.class);
        this.index = index;
        this.legacyBooleans = legacyBooleans;
        Component name = Component.text("Toast");
        Object serialized = legacyNames ? AdventureSerializer.getLegacyGsonSerializer().serialize(name) :
                optional ? name : LegacyComponentSerializer.legacySection().serialize(name);
        this.serialized = optional ? Optional.of(serialized) : serialized;
        this.type = optional ? EntityDataTypes.OPTIONAL_ADV_COMPONENT : EntityDataTypes.STRING;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        RabbitType rabbitType = entity.getProperty(this);
        if (rabbitType == null) return;
        if (!rabbitType.equals(RabbitType.TOAST)) {
            properties.put(index, legacyBooleans ?
                    newEntityData(index, EntityDataTypes.BYTE, (byte) rabbitType.getId()) :
                    newEntityData(index, EntityDataTypes.INT, rabbitType.getId()));
            properties.put(2, new EntityData(2, type, null));
        } else {
            properties.put(2, new EntityData(2, type, serialized));
        }
    }
}
