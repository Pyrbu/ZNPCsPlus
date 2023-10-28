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
     * Register a dummy property that can be used to store unique information per npc
     *
     * @param name The name of the new property
     * @param type The type of the new property
     */
    void registerDummy(String name, Class<?> type);
}
