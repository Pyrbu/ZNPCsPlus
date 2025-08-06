package lol.pyr.znpcsplus.api.entity;

import java.util.Collection;

/**
 * Class responsible for providing entity property keys
 * Some property keys are only registered in certain situations for example different minecraft versions
 */
public interface EntityPropertyRegistry {
    /**
     * @return All of the possible property keys
     */
    Collection<EntityProperty<?>> getAll();

    /**
     * Get a property key by it's name
     *
     * @param name The name of a property key
     * @return The property key corresponding to the name or null if there is none
     */
    EntityProperty<?> getByName(String name);

    /**
     * Get a property key by it's name and automatically cast the property to the proper type
     * If you don't know the type of the property you are requesting use {@link EntityPropertyRegistry#getByName(String)} instead
     *
     * @param name The name of a property key
     * @param type The class of the expected type of the returned property key
     * @return The property key corresponding to the name
     * @param <T> The expected type of the returned property key
     */
    <T> EntityProperty<T> getByName(String name, Class<T> type);

    /**
     * Register a dummy property that can be used to store unique information per npc<br>
     * Note: Properties registered this way will be player-modifiable by default
     *
     * @param name The name of the new property
     * @param type The type of the new property
     * @deprecated Use {@link #registerDummy(String, Class, boolean)} instead
     */
    @Deprecated
    default void registerDummy(String name, Class<?> type) {
        registerDummy(name, type, true);
    }

    /**
     * Register a dummy property that can be used to store unique information per npc
     *
     * @param name The name of the new property
     * @param type The type of the new property
     * @param playerModifiable Whether this property can be modified by players using commands
     */
    void registerDummy(String name, Class<?> type, boolean playerModifiable);

    /**
     * Register a dummy property with a default value (player-modifiable by default)
     *
     * @param name The name of the new property
     * @param defaultValue The default value for this property
     * @param <T> The type of the property value
     * @deprecated Use {@link #registerDummy(String, Object, boolean)} instead
     */
    default <T> void registerDummy(String name, T defaultValue) {
        registerDummy(name, defaultValue, true);
    }

    /**
     * Register a dummy property with a default value
     *
     * @param name The name of the new property
     * @param defaultValue The default value for this property
     * @param playerModifiable Whether this property can be modified by players using commands
     * @param <T> The type of the property value
     */
    <T> void registerDummy(String name, T defaultValue, boolean playerModifiable);
}
