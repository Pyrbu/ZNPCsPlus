package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.PapiUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class NameProperty extends EntityPropertyImpl<Component> {
    private final LegacyComponentSerializer legacySerializer;
    private final boolean legacySerialization;
    private final boolean optional;

    public NameProperty(LegacyComponentSerializer legacySerializer, boolean legacySerialization, boolean optional) {
        super("name", null, Component.class);
        this.legacySerializer = legacySerializer;

        this.legacySerialization = legacySerialization;
        this.optional = optional;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        Component value = entity.getProperty(this);
        if (value != null) {
            value = PapiUtil.set(legacySerializer, player, value);
            Object serialized = legacySerialization ? AdventureSerializer.getLegacyGsonSerializer().serialize(value) :
                    optional ? value : LegacyComponentSerializer.legacySection().serialize(value);
            if (optional) properties.put(2, new EntityData(2, EntityDataTypes.OPTIONAL_ADV_COMPONENT, Optional.of(serialized)));
            else properties.put(2, new EntityData(2, EntityDataTypes.STRING, serialized));
        }

        if (legacySerialization) properties.put(3, newEntityData(3, EntityDataTypes.BYTE, (byte) (value != null ? 1 : 0)));
        else properties.put(3, newEntityData(3, EntityDataTypes.BOOLEAN, value != null));
    }
}
