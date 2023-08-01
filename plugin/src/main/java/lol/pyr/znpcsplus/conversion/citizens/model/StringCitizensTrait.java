package lol.pyr.znpcsplus.conversion.citizens.model;

import lol.pyr.znpcsplus.npc.NpcImpl;
import org.jetbrains.annotations.NotNull;

public abstract class StringCitizensTrait extends CitizensTrait {
    public StringCitizensTrait(String identifier) {
        super(identifier);
    }

    @Override
    public @NotNull NpcImpl apply(NpcImpl npc, Object value) {
        if (!(value instanceof String)) return npc;
        return apply(npc, (String) value);
    }

    public abstract @NotNull NpcImpl apply(NpcImpl npc, String string);
}
