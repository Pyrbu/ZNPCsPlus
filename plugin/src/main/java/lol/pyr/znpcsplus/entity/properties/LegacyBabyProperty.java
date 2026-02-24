package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import java.util.Map;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

public class LegacyBabyProperty extends EntityPropertyImpl<Boolean> {
  private final int index;

  public LegacyBabyProperty(int index) {
    super("baby", false, Boolean.class);
    this.index = index;
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    boolean isBaby = entity.getProperty(this);
    if (entity.getType().equals(EntityTypes.ZOMBIE)) {
      properties.put(index, newEntityData(index, EntityDataTypes.BYTE, (byte) (isBaby ? 1 : 0)));
    } else {
      properties.put(index, newEntityData(index, EntityDataTypes.BYTE, (byte) (isBaby ? -1 : 0)));
    }
  }
}
