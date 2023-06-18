package lol.pyr.znpcsplus.api.npc;

import java.util.Collection;

public interface NpcTypeRegistry {
    NpcType getByName(String name);
    Collection<NpcType> getAll();
}
