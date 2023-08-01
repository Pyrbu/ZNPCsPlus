package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.conversion.citizens.model.SectionCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.util.VillagerLevel;
import lol.pyr.znpcsplus.util.VillagerType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class VillagerTrait extends SectionCitizensTrait {
    private final EntityPropertyRegistry registry;

    public VillagerTrait(EntityPropertyRegistry registry) {
        super("villagertrait");
        this.registry = registry;
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section) {
        int level = section.getInt("level");
        String type = section.getString("type", "plains");
        VillagerLevel villagerLevel;
        try {
            villagerLevel = VillagerLevel.values()[level];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            villagerLevel = VillagerLevel.STONE;
        }
        VillagerType villagerType;
        try {
            villagerType = VillagerType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            villagerType = VillagerType.PLAINS;
        }
        npc.setProperty(registry.getByName("villager_level", VillagerLevel.class), villagerLevel);
        npc.setProperty(registry.getByName("villager_type", VillagerType.class), villagerType);
        return npc;
    }
}
