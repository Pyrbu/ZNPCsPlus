package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class NameProperty extends EntityPropertyImpl<Component> {
    private final boolean legacy;
    private final boolean optionalComponent;

    public NameProperty() {
        super("name", null, Component.class);

        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        legacy = version.isOlderThan(ServerVersion.V_1_9);
        optionalComponent = version.isNewerThanOrEquals(ServerVersion.V_1_13);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        Component value = entity.getProperty(this);
        if (value != null) {
            String serialized = legacy ?
                    AdventureSerializer.getLegacyGsonSerializer().serialize(value) :
                    AdventureSerializer.getGsonSerializer().serialize(value);
            if (optionalComponent) properties.put(2, newEntityData(2, EntityDataTypes.OPTIONAL_COMPONENT, Optional.of(serialized)));
            else properties.put(2, newEntityData(2, EntityDataTypes.STRING, serialized));
        }

        if (legacy) properties.put(3, newEntityData(3, EntityDataTypes.BYTE, (byte) (value != null ? 1 : 0)));
        else properties.put(3, newEntityData(3, EntityDataTypes.BOOLEAN, value != null));
    }
}
