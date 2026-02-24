package lol.pyr.znpcsplus.storage;

import java.util.Collection;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;

public interface NpcStorage {
  Collection<NpcEntryImpl> loadNpcs();

  void saveNpcs(Collection<NpcEntryImpl> npcs);

  void deleteNpc(NpcEntryImpl npc);

  default void close() {}
}
