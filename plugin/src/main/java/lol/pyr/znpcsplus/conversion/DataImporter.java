package lol.pyr.znpcsplus.conversion;

import lol.pyr.znpcsplus.npc.NpcEntryImpl;

import java.util.Collection;

public interface DataImporter {
    Collection<NpcEntryImpl> importData();
    boolean isValid();
}
