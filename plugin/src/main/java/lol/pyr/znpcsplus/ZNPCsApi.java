package lol.pyr.znpcsplus;

import lol.pyr.znpcsplus.api.ZApi;
import lol.pyr.znpcsplus.api.npc.NPCRegistry;
import lol.pyr.znpcsplus.npc.NPCRegistryImpl;

public class ZNPCsApi implements ZApi {
    @Override
    public NPCRegistry getNPCRegistry() {
        return NPCRegistryImpl.get();
    }
}
