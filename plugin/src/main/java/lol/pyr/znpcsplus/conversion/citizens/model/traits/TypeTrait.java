package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.api.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.conversion.citizens.model.StringCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import org.jetbrains.annotations.NotNull;

public class TypeTrait extends StringCitizensTrait {
    private final NpcTypeRegistry registry;

    public TypeTrait(NpcTypeRegistry registry) {
        super("type");
        this.registry = registry;
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, String string) {
        NpcTypeImpl type = warpNpcType(string);
        if (type == null) return npc;
        npc.setType(type);
        return npc;
    }

    private NpcTypeImpl warpNpcType(String name) {
        name = name.toLowerCase();
//        if (name.equals("player")) name = "human";
//        else if (name.equals("zombievillager")) name = "zombie_villager";
        return (NpcTypeImpl) registry.getByName(name);
    }
}
