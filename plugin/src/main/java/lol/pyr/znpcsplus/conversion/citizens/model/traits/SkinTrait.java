package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.conversion.citizens.model.SectionCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.skin.SkinImpl;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class SkinTrait extends SectionCitizensTrait {
    private final EntityPropertyRegistry registry;

    public SkinTrait(EntityPropertyRegistry registry) {
        super("skintrait");
        this.registry = registry;
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section) {
        String texture = section.getString("textureRaw");
        String signature = section.getString("signature");
        if (texture != null && signature != null) npc.setProperty(registry.getByName("skin", SkinDescriptor.class), new PrefetchedDescriptor(new SkinImpl(texture, signature)));
        return npc;
    }
}
