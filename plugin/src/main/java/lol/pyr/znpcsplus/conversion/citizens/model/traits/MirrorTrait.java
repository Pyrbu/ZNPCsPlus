package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.conversion.citizens.model.SectionCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.skin.descriptor.MirrorDescriptor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class MirrorTrait extends SectionCitizensTrait {
    private final EntityPropertyRegistry registry;
    private final MojangSkinCache skinCache;

    public MirrorTrait(EntityPropertyRegistry registry, MojangSkinCache skinCache) {
        super("mirrortrait");
        this.registry = registry;
        this.skinCache = skinCache;
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section) {
        if (section.getBoolean("enabled")) npc.setProperty(registry.getByName("skin", SkinDescriptor.class), new MirrorDescriptor(skinCache));
        return npc;
    }
}
