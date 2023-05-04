package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.util.ZLocation;
import org.bukkit.World;

import java.util.Collection;

public interface NPCRegistry {
    Collection<? extends NPCEntry> all();
    Collection<String> ids();
    NPCEntry create(String id, World world, NPCType type, ZLocation location);
    NPCEntry get(String id);
    void delete(String id);
}