package lol.pyr.znpcsplus.conversion.citizens.model;

import lol.pyr.znpcsplus.npc.NpcImpl;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public abstract class SectionCitizensTrait extends CitizensTrait {
    public SectionCitizensTrait(String identifier) {
        super(identifier);
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, Object value) {
        if (!(value instanceof ConfigurationSection)) return npc;
        return apply(npc, (ConfigurationSection) value);
    }

    public abstract @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section);
}
