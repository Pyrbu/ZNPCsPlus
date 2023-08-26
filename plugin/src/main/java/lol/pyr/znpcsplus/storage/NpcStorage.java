package lol.pyr.znpcsplus.storage;

import lol.pyr.znpcsplus.npc.NpcEntryImpl;

import java.util.Collection;

public interface NpcStorage {
    Collection<NpcEntryImpl> loadNpcs();
    void saveNpcs(Collection<NpcEntryImpl> npcs);
    void deleteNpc(NpcEntryImpl npc);
}
