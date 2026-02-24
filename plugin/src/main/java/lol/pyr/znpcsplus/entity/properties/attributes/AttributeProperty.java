package lol.pyr.znpcsplus.entity.properties.attributes;

import com.github.retrooper.packetevents.protocol.attribute.Attribute;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import org.bukkit.entity.Player;

public class AttributeProperty extends EntityPropertyImpl<Double> {
  private final PacketFactory packetFactory;
  private final Attribute attribute;

  public AttributeProperty(PacketFactory packetFactory, String name, Attribute attribute) {
    super(name, attribute.getDefaultValue(), Double.class);
    this.packetFactory = packetFactory;
    this.attribute = attribute;
  }

  public double getMinValue() {
    return attribute.getMinValue();
  }

  public double getMaxValue() {
    return attribute.getMaxValue();
  }

  public double sanitizeValue(double value) {
    return attribute.sanitizeValue(value);
  }

  @Override
  public List<EntityData<?>> applyStandalone(
      Player player, PacketEntity packetEntity, boolean isSpawned) {
    apply(player, packetEntity, isSpawned, Collections.emptyList());
    return Collections.emptyList();
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {}

  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      List<WrapperPlayServerUpdateAttributes.Property> properties) {
    Double value = entity.getProperty(this);
    if (value == null) {
      return;
    }
    value = attribute.sanitizeValue(value);
    if (isSpawned) {
      packetFactory.sendAttribute(
          player,
          entity,
          new WrapperPlayServerUpdateAttributes.Property(
              attribute, value, Collections.emptyList()));
    } else {
      properties.add(
          new WrapperPlayServerUpdateAttributes.Property(
              attribute, value, Collections.emptyList()));
    }
  }

  public Attribute getAttribute() {
    return attribute;
  }
}
