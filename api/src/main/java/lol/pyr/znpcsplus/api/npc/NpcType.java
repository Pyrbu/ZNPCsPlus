package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.api.entity.EntityProperty;

import java.util.Set;

public interface NpcType {
    String getName();
    double getHologramOffset();
    Set<EntityProperty<?>> getAllowedProperties();
}
