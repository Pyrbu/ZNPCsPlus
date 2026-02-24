package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.EntityPositionData;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityHeadLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class V1_21_3PacketFactory extends V1_20_2PacketFactory {
  public V1_21_3PacketFactory(
      TaskScheduler scheduler,
      PacketEventsAPI<Plugin> packetEvents,
      EntityPropertyRegistryImpl propertyRegistry,
      LegacyComponentSerializer textSerializer,
      ConfigManager configManager) {
    super(scheduler, packetEvents, propertyRegistry, textSerializer, configManager);
  }

  @Override
  public void teleportEntity(Player player, PacketEntity entity) {
    NpcLocation location = entity.getLocation();
    sendPacket(
        player,
        new WrapperPlayServerEntityTeleport(
            entity.getEntityId(),
            new EntityPositionData(
                npcLocationToVector(location),
                new Vector3d(0, 0, 0),
                location.getYaw(),
                location.getPitch()),
            RelativeFlag.NONE,
            false));
    sendPacket(
        player, new WrapperPlayServerEntityHeadLook(entity.getEntityId(), location.getYaw()));
  }
}
