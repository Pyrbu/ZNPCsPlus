package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import java.util.Map;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NamedColor;
import org.bukkit.entity.Player;

public class GlowProperty extends EntityPropertyImpl<NamedColor> {
  private final PacketFactory packetFactory;

  public GlowProperty(PacketFactory packetFactory) {
    super("glow", null, NamedColor.class);
    this.packetFactory = packetFactory;
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    NamedColor value = entity.getProperty(this);
    EntityData<?> oldData = properties.get(0);
    //        byte oldValue = oldData == null ? 0 : (byte) oldData.getValue();
    byte oldValue = 0;
    if (oldData != null && oldData.getValue() instanceof Number) {
      oldValue = ((Number) oldData.getValue()).byteValue();
    }
    properties.put(
        0, newEntityData(0, EntityDataTypes.BYTE, (byte) (oldValue | (value == null ? 0 : 0x40))));
    // the team is already created with the right glow color in the packet factory if the npc isnt
    // spawned yet
    if (isSpawned) {
      packetFactory.removeTeam(player, entity);
      packetFactory.createTeam(player, entity, value);
    }
  }
}
