package lol.pyr.znpcsplus.api.entity;

import java.util.Set;

public interface PropertyHolder {
    <T> T getProperty(EntityProperty<T> key);
    boolean hasProperty(EntityProperty<?> key);
    <T> void setProperty(EntityProperty<T> key, T value);
    Set<EntityProperty<?>> getAppliedProperties();
}
