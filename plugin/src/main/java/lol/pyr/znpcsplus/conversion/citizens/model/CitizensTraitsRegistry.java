package lol.pyr.znpcsplus.conversion.citizens.model;

import java.util.HashMap;
import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.conversion.citizens.model.traits.*;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class CitizensTraitsRegistry {
  private final HashMap<String, CitizensTrait> traitMap = new HashMap<>();

  public CitizensTraitsRegistry(
      EntityPropertyRegistry propertyRegistry,
      MojangSkinCache skinCache,
      TaskScheduler taskScheduler,
      LegacyComponentSerializer textSerializer) {
    register(new LocationTrait());
    register(new ProfessionTrait(propertyRegistry));
    register(new VillagerTrait(propertyRegistry));
    register(new SkinTrait(propertyRegistry));
    register(new MirrorTrait(propertyRegistry, skinCache));
    register(new SkinLayersTrait(propertyRegistry));
    register(new LookTrait(propertyRegistry));
    register(new CommandTrait(taskScheduler));
    register(new HologramTrait(textSerializer));
    register(new EquipmentTrait(propertyRegistry));
    register(new SpawnedTrait());
  }

  public CitizensTrait getByName(String name) {
    return traitMap.get(name);
  }

  public void register(CitizensTrait trait) {
    traitMap.put(trait.getIdentifier(), trait);
  }
}
