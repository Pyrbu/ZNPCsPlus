package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class V1_16PacketFactory extends V1_14PacketFactory {
    public V1_16PacketFactory(TaskScheduler scheduler, MetadataFactory metadataFactory, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry) {
        super(scheduler, metadataFactory, packetEvents, propertyRegistry);
    }

    @Override
    public void sendEquipment(Player player, PacketEntity entity, PropertyHolder properties) {
        List<Equipment> equipments = generateEquipments(properties);
        if (equipments.size() > 0) sendPacket(player, new WrapperPlayServerEntityEquipment(entity.getEntityId(), equipments));
    }
}
