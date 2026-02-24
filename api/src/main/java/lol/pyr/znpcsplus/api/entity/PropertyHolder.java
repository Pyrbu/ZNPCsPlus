package lol.pyr.znpcsplus.api.entity;

import java.util.Set;
import org.bukkit.inventory.ItemStack;

/** Represents classes that have property values attatched to them */
public interface PropertyHolder {
  /**
   * Method used to get the value of a property from a property holder
   *
   * @param key Unique key representing a property
   * @return The value associated with the provided property key and this holder
   * @param <T> The type of the property value
   */
  <T> T getProperty(EntityProperty<T> key);

  /**
   * Method used to check if a property holder has a value set for a specific property key
   *
   * @param key Unique key representing a property
   * @return Whether this holder has a value set for the provided key
   */
  boolean hasProperty(EntityProperty<?> key);

  /**
   * Method used to set a value for the provided key on this property holder
   *
   * @param key Unique key representing a property
   * @param value The value to assign to the property key on this holder
   * @param <T> The type of the property value
   */
  <T> void setProperty(EntityProperty<T> key, T value);

  /**
   * Weird fix which is sadly required in order to not decrease performance when using item
   * properties, read https://github.com/Pyrbu/ZNPCsPlus/pull/129#issuecomment-1948777764
   *
   * @param key Unique key representing a property
   * @param value The value to assign to the property key on this holder
   */
  void setItemProperty(EntityProperty<?> key, ItemStack value);

  /**
   * Weird fix which is sadly required in order to not decrease performance when using item
   * properties, read https://github.com/Pyrbu/ZNPCsPlus/pull/129#issuecomment-1948777764
   *
   * @param key Unique key representing a property
   * @return the {@link ItemStack} associated with the provided property key and this holder
   */
  ItemStack getItemProperty(EntityProperty<?> key);

  /**
   * Method used to get a set of all of the property keys that this holder has a value for
   *
   * @return Set of property keys
   */
  Set<EntityProperty<?>> getAppliedProperties();
}
