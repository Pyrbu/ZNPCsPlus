package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.World;

import java.util.Collection;
import java.util.UUID;

public interface NpcRegistry {
    Collection<? extends NpcEntry> getAll();
    Collection<String> getAllIds();
    Collection<? extends NpcEntry> getAllPlayerMade();
    Collection<String> getAllPlayerMadeIds();
    NpcEntry create(String id, World world, NpcType type, NpcLocation location);
    NpcEntry getById(String id);
    NpcEntry getByUuid(UUID uuid);
    void delete(String id);
}
