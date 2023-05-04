package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.util.ZLocation;
import org.bukkit.World;

import java.util.Collection;

public interface NpcRegistry {
    Collection<? extends NpcEntry> all();
    Collection<String> ids();
    NpcEntry create(String id, World world, NpcType type, ZLocation location);
    NpcEntry get(String id);
    void delete(String id);
}
