package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import java.util.Map;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

public class BitsetProperty extends EntityPropertyImpl<Boolean> {
  private final int index;
  private final int bitmask;
  private final boolean inverted;
  private boolean integer = false;

  public BitsetProperty(String name, int index, int bitmask, boolean inverted, boolean integer) {
    this(name, index, bitmask, inverted);
    this.integer = integer;
  }

  public BitsetProperty(String name, int index, int bitmask, boolean inverted) {
    super(name, inverted, Boolean.class);
    this.index = index;
    this.bitmask = bitmask;
    this.inverted = inverted;
  }

  public BitsetProperty(String name, int index, int bitmask) {
    this(name, index, bitmask, false);
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    EntityData<?> oldData = properties.get(index);
    boolean enabled = entity.getProperty(this);
    if (inverted) enabled = !enabled;
    if (integer) {
      int oldValue = 0;
      if (oldData != null && oldData.getValue() instanceof Number) {
        oldValue = ((Number) oldData.getValue()).intValue();
      }
      properties.put(
          index, newEntityData(index, EntityDataTypes.INT, oldValue | (enabled ? bitmask : 0)));
    } else {
      byte oldValue = 0;
      if (oldData != null && oldData.getValue() instanceof Number) {
        oldValue = ((Number) oldData.getValue()).byteValue();
      }
      properties.put(
          index,
          newEntityData(index, EntityDataTypes.BYTE, (byte) (oldValue | (enabled ? bitmask : 0))));
    }
  }
}
