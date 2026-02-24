package lol.pyr.znpcsplus;

import lol.pyr.znpcsplus.api.NpcApi;
import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.interaction.ActionFactory;
import lol.pyr.znpcsplus.api.interaction.ActionRegistry;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptorFactory;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.interaction.ActionFactoryImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.serialization.NpcSerializerRegistryImpl;
import lol.pyr.znpcsplus.skin.SkinDescriptorFactoryImpl;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;

public class ZNpcsPlusApi implements NpcApi {
  private final NpcRegistryImpl npcRegistry;
  private final NpcTypeRegistryImpl typeRegistry;
  private final EntityPropertyRegistryImpl propertyRegistry;
  private final ActionRegistryImpl actionRegistry;
  private final ActionFactoryImpl actionFactory;
  private final SkinDescriptorFactoryImpl skinDescriptorFactory;
  private final NpcSerializerRegistryImpl npcSerializerRegistry;

  public ZNpcsPlusApi(
      NpcRegistryImpl npcRegistry,
      NpcTypeRegistryImpl typeRegistry,
      EntityPropertyRegistryImpl propertyRegistry,
      ActionRegistryImpl actionRegistry,
      ActionFactoryImpl actionFactory,
      MojangSkinCache skinCache,
      NpcSerializerRegistryImpl npcSerializerRegistry) {
    this.npcRegistry = npcRegistry;
    this.typeRegistry = typeRegistry;
    this.propertyRegistry = propertyRegistry;
    this.actionRegistry = actionRegistry;
    this.actionFactory = actionFactory;
    this.skinDescriptorFactory = new SkinDescriptorFactoryImpl(skinCache);
    this.npcSerializerRegistry = npcSerializerRegistry;
  }

  @Override
  public NpcRegistry getNpcRegistry() {
    return npcRegistry;
  }

  @Override
  public NpcTypeRegistry getNpcTypeRegistry() {
    return typeRegistry;
  }

  @Override
  public EntityPropertyRegistry getPropertyRegistry() {
    return propertyRegistry;
  }

  @Override
  public ActionRegistry getActionRegistry() {
    return actionRegistry;
  }

  @Override
  public ActionFactory getActionFactory() {
    return actionFactory;
  }

  @Override
  public SkinDescriptorFactory getSkinDescriptorFactory() {
    return skinDescriptorFactory;
  }

  @Override
  public NpcSerializerRegistryImpl getNpcSerializerRegistry() {
    return npcSerializerRegistry;
  }
}
