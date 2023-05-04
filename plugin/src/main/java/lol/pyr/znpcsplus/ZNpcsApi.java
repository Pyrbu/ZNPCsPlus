package lol.pyr.znpcsplus;

import lol.pyr.znpcsplus.api.ZApi;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

public class ZNpcsApi implements ZApi {
    @Override
    public NpcRegistry getNpcRegistry() {
        return NpcRegistryImpl.get();
    }
}
