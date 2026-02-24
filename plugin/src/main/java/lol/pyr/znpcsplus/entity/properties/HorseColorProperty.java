package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import java.util.Map;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.HorseColor;
import org.bukkit.entity.Player;

public class HorseColorProperty extends EntityPropertyImpl<HorseColor> {
  private final int index;

  public HorseColorProperty(int index) {
    super("horse_color", HorseColor.WHITE, HorseColor.class);
    this.index = index;
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    EntityData<?> oldData = properties.get(index);
    HorseColor value = entity.getProperty(this);
    int oldValue =
        (oldData != null && oldData.getValue() instanceof Integer)
            ? (Integer) oldData.getValue()
            : 0;

    int newValue = value.ordinal() | (oldValue & 0xFF00);
    properties.put(index, newEntityData(index, EntityDataTypes.INT, newValue));
  }
}
