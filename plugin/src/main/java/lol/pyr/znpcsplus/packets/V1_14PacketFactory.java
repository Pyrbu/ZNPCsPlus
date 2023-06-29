package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.NpcPose;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class V1_14PacketFactory extends V1_11PacketFactory {
    public V1_14PacketFactory(TaskScheduler scheduler, MetadataFactory metadataFactory, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer) {
        super(scheduler, metadataFactory, packetEvents, propertyRegistry, textSerializer);
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
