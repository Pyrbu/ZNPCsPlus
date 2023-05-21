package lol.pyr.znpcsplus;

import lol.pyr.znpcsplus.api.ZApi;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

public class ZNPCsPlusApi implements ZApi {
    private final NpcRegistryImpl npcRegistry;

    public ZNPCsPlusApi(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public NpcRegistry getNpcRegistry() {
        return npcRegistry;
    }
}
