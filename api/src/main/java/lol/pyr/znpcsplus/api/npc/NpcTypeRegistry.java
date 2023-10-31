package lol.pyr.znpcsplus.api.npc;

import java.util.Collection;

/**
 * Base for NpcType registries.
 */
public interface NpcTypeRegistry {
    /**
     * Gets a NPC type by name.
     * @param name The name of the NPC type.
     * @return The type that is represented by the name or null if it doesnt exist
     */
    NpcType getByName(String name);

    /**
     * Gets all NPC types.
     * @return all of the npc types
     */
    Collection<NpcType> getAll();
}
