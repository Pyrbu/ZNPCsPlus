package lol.pyr.znpcsplus;

import lol.pyr.znpcsplus.api.ZApi;
import lol.pyr.znpcsplus.api.NPCRegistry;
import lol.pyr.znpcsplus.api.npc.NPC;
import lol.pyr.znpcsplus.api.npc.NPCType;
import lol.pyr.znpcsplus.util.ZLocation;
import org.bukkit.World;

public class ZNPCsApi implements ZApi {
    @Override
    public NPCRegistry getNPCRegistry() {
        return lol.pyr.znpcsplus.npc.NPCRegistry.get();
    }

    @Override
    public NPC createNPC(World world, NPCType type, ZLocation location) {
        return new lol.pyr.znpcsplus.npc.NPC(world, type, location);
    }
}
