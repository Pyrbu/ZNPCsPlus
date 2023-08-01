package lol.pyr.znpcsplus.conversion.citizens.model;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.conversion.citizens.model.traits.*;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;

import java.util.HashMap;

public class CitizensTraitsRegistry {
    private final HashMap<String, CitizensTrait> traitMap = new HashMap<>();

    public CitizensTraitsRegistry(NpcTypeRegistry typeRegistry, EntityPropertyRegistry propertyRegistry, MojangSkinCache skinCache) {
        register(new LocationTrait());
        register(new TypeTrait(typeRegistry));
        register(new ProfessionTrait(propertyRegistry));
        register(new VillagerTrait(propertyRegistry));
        register(new SkinTrait(propertyRegistry));
        register(new MirrorTrait(propertyRegistry, skinCache));
        register(new SkinLayersTrait(propertyRegistry));
        register(new LookTrait(propertyRegistry));
    }

    public CitizensTrait getByName(String name) {
        return traitMap.get(name);
    }

    public void register(CitizensTrait trait) {
        traitMap.put(trait.getIdentifier(), trait);
    }
}
