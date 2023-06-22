package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class V1_17PacketFactory extends V1_16PacketFactory {
    public V1_17PacketFactory(TaskScheduler scheduler, MetadataFactory metadataFactory, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry) {
        super(scheduler, metadataFactory, packetEvents, propertyRegistry);
    }

    @Override
    public Map<Integer, EntityData> generateMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        Map<Integer, EntityData> data = super.generateMetadata(player, entity, properties);
        add(data, metadataFactory.effects(properties.getProperty(propertyRegistry.getByName("fire", Boolean.class)),
                properties.hasProperty(propertyRegistry.getByName("glow", Boolean.class)),
                properties.getProperty(propertyRegistry.getByName("invisible", Boolean.class)),
                false));
        if (properties.getProperty(propertyRegistry.getByName("shaking", Boolean.class))) add(data, metadataFactory.shaking());
        return data;
    }
}
