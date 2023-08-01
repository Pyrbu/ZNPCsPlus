package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.conversion.citizens.model.SectionCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class LocationTrait extends SectionCitizensTrait {
    public LocationTrait() {
        super("location");
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section) {
        double x = Double.parseDouble(section.getString("x"));
        double y = Double.parseDouble(section.getString("y"));
        double z = Double.parseDouble(section.getString("z"));
        float yaw = Float.parseFloat(section.getString("yaw"));
        float pitch = Float.parseFloat(section.getString("pitch"));
        NpcLocation location = new NpcLocation(x, y, z, yaw, pitch);
        npc.setLocation(location);
        return npc;
    }
}
