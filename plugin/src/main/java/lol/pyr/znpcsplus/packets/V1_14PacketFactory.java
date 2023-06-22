package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.NpcLocation;
import lol.pyr.znpcsplus.util.NpcPose;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Optional;

public class V1_14PacketFactory extends V1_10PacketFactory {
    public V1_14PacketFactory(TaskScheduler scheduler, MetadataFactory metadataFactory, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry) {
        super(scheduler, metadataFactory, packetEvents, propertyRegistry);
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
        add(data, metadataFactory.pose(adaptNpcPose(properties.getProperty(propertyRegistry.getByName("pose", NpcPose.class)))));
        return data;
    }

    protected EntityPose adaptNpcPose(NpcPose pose) {
        return EntityPose.valueOf(pose.name());
    }
}
