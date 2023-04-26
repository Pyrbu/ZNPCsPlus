package lol.pyr.znpcsplus.api;

import lol.pyr.znpcsplus.api.npc.EntityProperty;

public interface PropertyHolder {
    <T> T getProperty(EntityProperty<T> key);
    boolean hasProperty(EntityProperty<?> key);
}
