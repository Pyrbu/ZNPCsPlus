package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.NamedColor;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Optional;


public class V1_17PacketFactory extends V1_8PacketFactory {
    public V1_17PacketFactory(TaskScheduler scheduler, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer, ConfigManager configManager) {
        super(scheduler, packetEvents, propertyRegistry, textSerializer, configManager);
    }

    @Override
    public void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        NpcLocation location = entity.getLocation();
        sendPacket(player, new WrapperPlayServerSpawnEntity(entity.getEntityId(), Optional.of(entity.getUuid()), entity.getType(),
                npcLocationToVector(location), location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.of(new Vector3d())));

        // Sets the nickname of an entity other than the player. If the display_name attribute is empty, the default entity name is used.
        // V1_8PacketFactory spawnEntity method
        // I am currently playing version 1.21.1, and I am busy, so it is not very convenient for me to test it.
        if (displayNameProperty != null && properties.hasProperty(displayNameProperty.get())) {
            EntityData<Optional<Component>> nameData = new EntityData<>(
                    2,
                    EntityDataTypes.OPTIONAL_ADV_COMPONENT,
                    Optional.of(Component.text(properties.getProperty(displayNameProperty.get())))
            );
            sendPacket(player, new WrapperPlayServerEntityMetadata(
                    entity.getEntityId(),
                    Collections.singletonList(nameData)
            ));
        }
        sendAllMetadata(player, entity, properties);
        if (EntityTypes.isTypeInstanceOf(entity.getType(), EntityTypes.LIVINGENTITY)) sendAllAttributes(player, entity, properties);
        createTeam(player, entity, properties.getProperty(propertyRegistry.getByName("glow", NamedColor.class)));
    }
}
