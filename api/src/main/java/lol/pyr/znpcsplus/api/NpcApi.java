package lol.pyr.znpcsplus.api;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.interaction.ActionFactory;
import lol.pyr.znpcsplus.api.interaction.ActionRegistry;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.api.serialization.NpcSerializerRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptorFactory;

/** Main API class for ZNPCsPlus. */
public interface NpcApi {
  /**
   * Gets the NPC registry.
   *
   * @return the NPC registry
   */
  NpcRegistry getNpcRegistry();

  /**
   * Gets the NPC type registry.
   *
   * @return the NPC type registry
   */
  NpcTypeRegistry getNpcTypeRegistry();

  /**
   * Gets the entity property registry.
   *
   * @return the entity property registry
   */
  EntityPropertyRegistry getPropertyRegistry();

  /**
   * Gets the action registry.
   *
   * @return the action registry
   */
  ActionRegistry getActionRegistry();

  /**
   * Gets the action factory.
   *
   * @return the action factory
   */
  ActionFactory getActionFactory();

  /**
   * Gets the skin descriptor factory.
   *
   * @return the skin descriptor factory
   */
  SkinDescriptorFactory getSkinDescriptorFactory();

  /**
   * Gets the npc serializer registry.
   *
   * @return the npc serializer registry
   */
  NpcSerializerRegistry getNpcSerializerRegistry();
}
