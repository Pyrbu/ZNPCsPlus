package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import java.util.Optional;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.NamedColor;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class V1_17PacketFactory extends V1_8PacketFactory {
  public V1_17PacketFactory(
      TaskScheduler scheduler,
      PacketEventsAPI<Plugin> packetEvents,
      EntityPropertyRegistryImpl propertyRegistry,
      LegacyComponentSerializer textSerializer,
      ConfigManager configManager) {
    super(scheduler, packetEvents, propertyRegistry, textSerializer, configManager);
  }

  @Override
  public void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties) {
    NpcLocation location = entity.getLocation();
    sendPacket(
        player,
        new WrapperPlayServerSpawnEntity(
            entity.getEntityId(),
            Optional.of(entity.getUuid()),
            entity.getType(),
            npcLocationToVector(location),
            location.getPitch(),
            location.getYaw(),
            location.getYaw(),
            0,
            Optional.of(new Vector3d())));
    sendAllMetadata(player, entity, properties);
    if (EntityTypes.isTypeInstanceOf(entity.getType(), EntityTypes.LIVINGENTITY))
      sendAllAttributes(player, entity, properties);
    createTeam(
        player,
        entity,
        properties.getProperty(propertyRegistry.getByName("glow", NamedColor.class)));
  }
}
