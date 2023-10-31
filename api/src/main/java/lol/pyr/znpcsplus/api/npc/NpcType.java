package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.api.entity.EntityProperty;

import java.util.Set;

/**
 * Represents a type of NPC.
 * This defines the {@link org.bukkit.entity.EntityType} of the NPC, as well as the properties that are allowed to be set on the NPC.
 */
public interface NpcType {
    /**
     * The name of the NPC type.
     * @return The name of the NPC type.
     */
    String getName();

    /**
     * The offset of the hologram above the NPC.
     * @return the offset
     */
    double getHologramOffset();

    /**
     * Set of properties that are allowed to be set on the NPC.
     * @return allowed properties
     */
    Set<EntityProperty<?>> getAllowedProperties();
}
