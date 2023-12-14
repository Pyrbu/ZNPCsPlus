package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class DinnerboneProperty extends EntityPropertyImpl<Boolean> {
    private final Object serialized;
    private final EntityDataType<?> type;

    public DinnerboneProperty(boolean legacy, boolean optional) {
        super("dinnerbone", false, Boolean.class);
        Component name = Component.text("Dinnerbone");
        Object serialized = legacy ? AdventureSerializer.getLegacyGsonSerializer().serialize(name) :
                optional ? name : LegacyComponentSerializer.legacySection().serialize(name);
        this.serialized = optional ? Optional.of(serialized) : serialized;
        this.type = optional ? EntityDataTypes.OPTIONAL_ADV_COMPONENT : EntityDataTypes.STRING;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        properties.put(2, new EntityData(2, type, entity.getProperty(this) ? serialized : null));
    }
}
