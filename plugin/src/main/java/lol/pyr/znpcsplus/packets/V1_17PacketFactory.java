package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Optional;

public class V1_17PacketFactory extends V1_16PacketFactory {
    public V1_17PacketFactory(TaskScheduler scheduler, MetadataFactory metadataFactory, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer) {
        super(scheduler, metadataFactory, packetEvents, propertyRegistry, textSerializer);
    }

    @Override
    public void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        NpcLocation location = entity.getLocation();
        sendPacket(player, new WrapperPlayServerSpawnEntity(entity.getEntityId(), Optional.of(entity.getUuid()), entity.getType(),
                npcLocationToVector(location), location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.of(new Vector3d())));
        sendAllMetadata(player, entity, properties);
        createTeam(player, entity, properties);
    }

    @Override
    public Map<Integer, EntityData> generateMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        Map<Integer, EntityData> data = super.generateMetadata(player, entity, properties);
        add(data, metadataFactory.shaking(properties.getProperty(propertyRegistry.getByName("shaking", Boolean.class))));
        return data;
    }
}
