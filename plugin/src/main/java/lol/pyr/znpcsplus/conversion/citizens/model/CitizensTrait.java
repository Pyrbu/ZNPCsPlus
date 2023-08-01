package lol.pyr.znpcsplus.conversion.citizens.model;

import lol.pyr.znpcsplus.npc.NpcImpl;
import org.jetbrains.annotations.NotNull;

public abstract class CitizensTrait {
    private final String identifier;

    public CitizensTrait(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract @NotNull NpcImpl apply(NpcImpl npc, Object value);

}
