package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import java.util.Map;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.HorseStyle;
import org.bukkit.entity.Player;

public class HorseStyleProperty extends EntityPropertyImpl<HorseStyle> {
  private final int index;

  public HorseStyleProperty(int index) {
    super("horse_style", HorseStyle.NONE, HorseStyle.class);
    this.index = index;
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    EntityData<?> oldData = properties.get(index);
    HorseStyle value = entity.getProperty(this);

    int oldValue =
        (oldData != null && oldData.getValue() instanceof Integer)
            ? (Integer) oldData.getValue()
            : 0;
    int newValue = (oldValue & 0x00FF) | (value.ordinal() << 8);
    properties.put(index, newEntityData(index, EntityDataTypes.INT, newValue));
  }
}
