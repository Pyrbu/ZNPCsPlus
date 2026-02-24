package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import java.util.Map;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

public class HealthProperty extends EntityPropertyImpl<Float> {
  private final int index;

  public HealthProperty(int index) {
    super("health", 20f, Float.class);
    this.index = index;
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    float health = entity.getProperty(this);
    health = (float) Attributes.MAX_HEALTH.sanitizeValue(health);
    properties.put(index, new EntityData<>(index, EntityDataTypes.FLOAT, health));
  }
}
