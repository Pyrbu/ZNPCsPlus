package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import java.util.Map;
import lol.pyr.znpcsplus.entity.ArmorStandVehicleProperties;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import org.bukkit.entity.Player;

public class EntitySittingProperty extends EntityPropertyImpl<Boolean> {
  private final PacketFactory packetFactory;
  private final EntityPropertyRegistryImpl propertyRegistry;

  public EntitySittingProperty(
      PacketFactory packetFactory, EntityPropertyRegistryImpl propertyRegistry) {
    super("entity_sitting", false, Boolean.class);
    this.packetFactory = packetFactory;
    this.propertyRegistry = propertyRegistry;
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    boolean sitting = entity.getProperty(this);
    if (sitting) {
      if (entity.getVehicle() == null) {
        PacketEntity vehiclePacketEntity =
            new PacketEntity(
                packetFactory,
                new ArmorStandVehicleProperties(propertyRegistry),
                entity.getViewable(),
                EntityTypes.ARMOR_STAND,
                entity.getLocation().withY(entity.getLocation().getY() - 0.9));
        entity.setVehicle(vehiclePacketEntity);
      }
    } else if (entity.getVehicle() != null) {
      entity.setVehicle(null);
    }
  }
}
