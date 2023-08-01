package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.conversion.citizens.model.SectionCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SkinLayersTrait extends SectionCitizensTrait {
    private final EntityPropertyRegistry registry;
    private final Map<String, String> skinLayers;

    public SkinLayersTrait(EntityPropertyRegistry registry) {
        super("skinlayers");
        this.registry = registry;
        this.skinLayers = new HashMap<>();
        this.skinLayers.put("cape", "skin_cape");
        this.skinLayers.put("hat", "skin_hat");
        this.skinLayers.put("jacket", "skin_jacket");
        this.skinLayers.put("left_sleeve", "skin_left_sleeve");
        this.skinLayers.put("left_pants", "skin_left_leg");
        this.skinLayers.put("right_sleeve", "skin_right_sleeve");
        this.skinLayers.put("right_pants", "skin_right_leg");
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section) {
        for (Map.Entry<String, String> entry : this.skinLayers.entrySet()) {
            String key = entry.getKey();
            String property = entry.getValue();
            if (section.contains(key)) npc.setProperty(registry.getByName(property, Boolean.class), section.getBoolean(key));
        }
        return npc;
    }
}
