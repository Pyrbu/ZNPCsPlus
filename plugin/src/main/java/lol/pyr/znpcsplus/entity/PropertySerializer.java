package lol.pyr.znpcsplus.entity;

public interface PropertySerializer<T> {
    String serialize(T property);
    T deserialize(String property);
    Class<T> getTypeClass();
}