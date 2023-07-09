package lol.pyr.znpcsplus.entity;

public interface PropertySerializer<T> {
    String serialize(T property);
    T deserialize(String property);
    Class<T> getTypeClass();

    @SuppressWarnings("unchecked")
    default String UNSAFE_serialize(Object property) {
        return serialize((T) property);
    }
}