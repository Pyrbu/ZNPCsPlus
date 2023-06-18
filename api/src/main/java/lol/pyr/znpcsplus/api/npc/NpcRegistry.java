package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.World;

import java.util.Collection;

public interface NpcRegistry {
    Collection<? extends NpcEntry> getAll();
    Collection<String> getAllIds();
    Collection<? extends NpcEntry> getAllPlayerMade();
    Collection<String> getAllPlayerMadeIds();
    NpcEntry create(String id, World world, NpcType type, NpcLocation location);
    NpcEntry get(String id);
    void delete(String id);
}
