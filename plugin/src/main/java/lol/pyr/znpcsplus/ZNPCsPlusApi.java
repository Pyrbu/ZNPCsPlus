package lol.pyr.znpcsplus;

import lol.pyr.znpcsplus.api.NpcApi;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

public class ZNPCsPlusApi implements NpcApi {
    private final NpcRegistryImpl npcRegistry;

    public ZNPCsPlusApi(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public NpcRegistry getNpcRegistry() {
        return npcRegistry;
    }
}
