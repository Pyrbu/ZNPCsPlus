package lol.pyr.znpcsplus;

import lol.pyr.znpcsplus.api.ZApi;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.npc.NPCRegistryImpl;

public class ZNPCsAPI implements ZApi {
    @Override
    public NpcRegistry getNpcRegistry() {
        return NPCRegistryImpl.get();
    }
}
