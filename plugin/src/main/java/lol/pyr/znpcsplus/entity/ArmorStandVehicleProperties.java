package lol.pyr.znpcsplus.entity;

import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an armor stand vehicle entity.
 *
 * <p>This entity is used to make the NPC sit on an invisible armor stand.
 */
public class ArmorStandVehicleProperties implements PropertyHolder {

  private final Map<EntityPropertyImpl<?>, Object> propertyMap = new HashMap<>();

  public ArmorStandVehicleProperties(EntityPropertyRegistryImpl propertyRegistry) {
    _setProperty(propertyRegistry.getByName("small", Boolean.class), true);
    _setProperty(propertyRegistry.getByName("invisible", Boolean.class), true);
    _setProperty(propertyRegistry.getByName("base_plate", Boolean.class), false);
  }

  @SuppressWarnings("unchecked")
  public <T> T getProperty(EntityProperty<T> key) {
    return hasProperty(key)
        ? (T) propertyMap.get((EntityPropertyImpl<?>) key)
        : key.getDefaultValue();
  }

  @Override
  public boolean hasProperty(EntityProperty<?> key) {
    return propertyMap.containsKey((EntityPropertyImpl<?>) key);
  }

  @SuppressWarnings("unchecked")
  private <T> void _setProperty(EntityProperty<T> key, T value) {
    Object val = value;
    if (val instanceof ItemStack) val = SpigotConversionUtil.fromBukkitItemStack((ItemStack) val);

    setProperty((EntityPropertyImpl<T>) key, (T) val);
  }

  @Override
  public <T> void setProperty(EntityProperty<T> key, T value) {
    throw new UnsupportedOperationException("Cannot set properties on armor stands");
  }

  @Override
  public void setItemProperty(EntityProperty<?> key, ItemStack value) {
    throw new UnsupportedOperationException("Cannot set item properties on armor stands");
  }

  @Override
  public ItemStack getItemProperty(EntityProperty<?> key) {
    throw new UnsupportedOperationException("Cannot get item properties on armor stands");
  }

  public <T> void setProperty(EntityPropertyImpl<T> key, T value) {
    if (key == null) return;
    if (value == null || value.equals(key.getDefaultValue())) propertyMap.remove(key);
    else propertyMap.put(key, value);
  }

  public Set<EntityProperty<?>> getAllProperties() {
    return Collections.unmodifiableSet(propertyMap.keySet());
  }

  @Override
  public Set<EntityProperty<?>> getAppliedProperties() {
    return Collections.unmodifiableSet(propertyMap.keySet());
  }
}
