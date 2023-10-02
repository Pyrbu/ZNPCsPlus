package lol.pyr.znpcsplus.api.entity;

import java.util.Collection;

public interface EntityPropertyRegistry {
    Collection<EntityProperty<?>> getAll();
    EntityProperty<?> getByName(String name);
    <T> EntityProperty<T> getByName(String name, Class<T> type);
    void registerDummy(String name, Class<?> type);
}
