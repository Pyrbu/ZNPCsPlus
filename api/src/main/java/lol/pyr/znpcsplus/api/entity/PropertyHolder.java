package lol.pyr.znpcsplus.api.entity;

public interface PropertyHolder {
    <T> T getProperty(EntityProperty<T> key);
    boolean hasProperty(EntityProperty<?> key);
}
