package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import java.util.Map;
import java.util.Optional;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class DinnerboneProperty extends EntityPropertyImpl<Boolean> {
  private final boolean optional;
  private final Object serialized;

  public DinnerboneProperty(boolean legacy, boolean optional) {
    super("dinnerbone", false, Boolean.class);
    this.optional = optional;
    Component name = Component.text("Dinnerbone");
    this.serialized =
        legacy
            ? AdventureSerializer.serializer().legacy().serialize(name)
            : optional ? name : LegacyComponentSerializer.legacySection().serialize(name);
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    if (optional) {
      properties.put(
          2,
          new EntityData<>(
              2,
              EntityDataTypes.OPTIONAL_ADV_COMPONENT,
              entity.getProperty(this) ? Optional.of((Component) serialized) : Optional.empty()));
    } else {
      properties.put(
          2,
          new EntityData<>(
              2, EntityDataTypes.STRING, entity.getProperty(this) ? (String) serialized : ""));
    }
  }
}
