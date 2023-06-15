package lol.pyr.znpcsplus.api.entity;

public interface PropertyHolder {
    <T> T getProperty(EntityProperty<T> key);
    boolean hasProperty(EntityProperty<?> key);
    <T> void setProperty(EntityProperty<T> key, T value);

    <T> void UNSAFE_setProperty(EntityProperty<?> property, Object value);
}
