package lol.pyr.znpcsplus.api.entity;

/**
 * Class that represents a unique property
 * @param <T> The type of the value of this property
 */
public interface EntityProperty<T> {
    /**
     * The default value of this property, if this is provided in {@link PropertyHolder#setProperty(EntityProperty, Object)}
     * as the value the property will be removed from the holder
     *
     * @return The default value of this property
     */
    T getDefaultValue();

    /**
     * @return The name of this property
     */
    String getName();

    /**
     * @return Whether this property can be modified by players using commands
     */
    boolean isPlayerModifiable();
}
