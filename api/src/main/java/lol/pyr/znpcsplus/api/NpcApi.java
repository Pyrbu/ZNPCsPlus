package lol.pyr.znpcsplus.api;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptorFactory;

/**
 * Main API class for ZNPCsPlus.
 */
public interface NpcApi {
    /**
     * Gets the NPC registry.
     * @return the NPC registry
     */
    NpcRegistry getNpcRegistry();

    /**
     * Gets the NPC type registry.
     * @return the NPC type registry
     */
    NpcTypeRegistry getNpcTypeRegistry();

    /**
     * Gets the entity property registry.
     * @return the entity property registry
     */
    EntityPropertyRegistry getPropertyRegistry();

    /**
     * Gets the skin descriptor factory.
     * @return the skin descriptor factory
     */
    SkinDescriptorFactory getSkinDescriptorFactory();
}
