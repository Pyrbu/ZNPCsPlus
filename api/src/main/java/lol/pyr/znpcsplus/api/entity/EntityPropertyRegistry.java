package lol.pyr.znpcsplus.api.entity;

public interface EntityPropertyRegistry {
    EntityProperty<?> getByName(String name);
    <T> EntityProperty<T> getByName(String name, Class<T> type);
}
