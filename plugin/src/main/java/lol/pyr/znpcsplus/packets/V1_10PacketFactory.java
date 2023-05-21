package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class V1_10PacketFactory extends V1_9PacketFactory {
    public V1_10PacketFactory(TaskScheduler scheduler, MetadataFactory metadataFactory, PacketEventsAPI<Plugin> packetEvents) {
        super(scheduler, metadataFactory, packetEvents);
    }

    @Override
    public Map<Integer, EntityData> generateMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        Map<Integer, EntityData> data = super.generateMetadata(player, entity, properties);
        add(data, metadataFactory.noGravity());
        return data;
    }
}
