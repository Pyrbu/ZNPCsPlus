package lol.pyr.znpcsplus.conversion;

import java.util.Collection;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;

public interface DataImporter {
  Collection<NpcEntryImpl> importData();

  boolean isValid();
}
