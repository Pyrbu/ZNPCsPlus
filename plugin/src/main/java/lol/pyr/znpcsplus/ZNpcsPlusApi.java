package lol.pyr.znpcsplus;

import lol.pyr.znpcsplus.api.NpcApi;
import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptorFactory;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.skin.SkinDescriptorFactoryImpl;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;

public class ZNpcsPlusApi implements NpcApi {
    private final NpcRegistryImpl npcRegistry;
    private final NpcTypeRegistryImpl typeRegistry;
    private final EntityPropertyRegistryImpl propertyRegistry;
    private final SkinDescriptorFactoryImpl skinDescriptorFactory;

    public ZNpcsPlusApi(NpcRegistryImpl npcRegistry, NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, MojangSkinCache skinCache) {
        this.npcRegistry = npcRegistry;
        this.typeRegistry = typeRegistry;
        this.propertyRegistry = propertyRegistry;
        this.skinDescriptorFactory = new SkinDescriptorFactoryImpl(skinCache);
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
    public SkinDescriptorFactory getSkinDescriptorFactory() {
        return skinDescriptorFactory;
    }
}
