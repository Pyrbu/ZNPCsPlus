package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.World;

import java.util.Collection;
import java.util.UUID;

/**
 * Base class for all NPC registries
 */
public interface NpcRegistry {

    /**
     * Gets all NPC entries
     * @return All NPC entries
     */
    Collection<? extends NpcEntry> getAll();

    /**
     * Gets all NPC IDs
     * @return All NPC IDs
     */
    Collection<String> getAllIds();

    /**
     * Gets all NPC entries that are player made
     * @return All player made NPC entries
     */
    Collection<? extends NpcEntry> getAllPlayerMade();

    /**
     * Gets IDs of all player made NPCs
     * @return IDs of all player made NPCs
     */
    Collection<String> getAllPlayerMadeIds();

    /**
     * Creates a new NPC entry
     * @param id The ID of the NPC entry
     * @param world The {@link World} of the NPC entry
     * @param type The {@link NpcType} of the NPC entry
     * @param location The {@link NpcLocation} of the NPC entry
     * @return The entry of the newly created npc
     */
    NpcEntry create(String id, World world, NpcType type, NpcLocation location);

    /**
     * Gets an NPC entry by its ID
     * @param id The ID of the NPC entry
     * @return The NPC entry
     */
    NpcEntry getById(String id);

    /**
     * Gets an NPC entry by its UUID
     * @param uuid The UUID of the NPC entry
     * @return The NPC entry
     */
    NpcEntry getByUuid(UUID uuid);

    /**
     * Deletes an NPC entry by its ID
     * @param id The ID of the NPC entry
     */
    void delete(String id);
}
