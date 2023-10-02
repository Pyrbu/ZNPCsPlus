package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.conversion.citizens.model.SectionCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.util.LookType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class LookTrait extends SectionCitizensTrait {
    private final EntityPropertyRegistry registry;

    public LookTrait(EntityPropertyRegistry registry) {
        super("lookclose");
        this.registry = registry;
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section) {
        if (section.getBoolean("enabled")) npc.setProperty(registry.getByName("look", LookType.class), LookType.CLOSEST_PLAYER);
        return npc;
    }
}
