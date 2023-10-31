package lol.pyr.znpcsplus.api.npc;

import java.util.Collection;

/**
 * Base for NpcType registries.
 */
public interface NpcTypeRegistry {
    /**
     * Gets a NPC type by name.
     * @param name The name of the NPC type.
     */
    NpcType getByName(String name);

    /**
     * Gets all NPC types.
     */
    Collection<NpcType> getAll();
}
