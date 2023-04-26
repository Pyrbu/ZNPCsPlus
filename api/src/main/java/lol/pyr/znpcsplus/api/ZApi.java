package lol.pyr.znpcsplus.api;

import lol.pyr.znpcsplus.api.npc.NPC;
import lol.pyr.znpcsplus.api.npc.NPCType;
import lol.pyr.znpcsplus.util.ZLocation;
import org.bukkit.World;

public interface ZApi {
    NPCRegistry getNPCRegistry();
    NPC createNPC(World world, NPCType type, ZLocation location);
}
