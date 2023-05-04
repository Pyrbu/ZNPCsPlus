package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.api.npc.NPCRegistry;
import lol.pyr.znpcsplus.api.npc.NPCType;
import lol.pyr.znpcsplus.util.ZLocation;
import org.bukkit.World;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NPCRegistryImpl implements NPCRegistry {
    private final static NPCRegistryImpl registry = new NPCRegistryImpl();

    public static NPCRegistryImpl get() {
        return registry;
    }

    private NPCRegistryImpl() {
        if (registry != null) throw new UnsupportedOperationException("This class can only be instanciated once!");
    }

    private final Map<String, NPCEntryImpl> npcMap = new HashMap<>();

    public NPCEntryImpl get(String id) {
        return npcMap.get(id.toUpperCase());
    }

    public Collection<NPCEntryImpl> all() {
        return Collections.unmodifiableCollection(npcMap.values());
    }

    public NPCEntryImpl getByEntityId(int id) {
        return all().stream().filter(entry -> entry.getNpc().getEntity().getEntityId() == id).findFirst().orElse(null);
    }

    public Collection<String> ids() {
        return Collections.unmodifiableSet(npcMap.keySet());
    }

    @Override
    public NPCEntryImpl create(String id, World world, NPCType type, ZLocation location) {
        id = id.toUpperCase();
        if (npcMap.containsKey(id)) throw new IllegalArgumentException("An npc with the id " + id + " already exists!");
        NPCImpl npc = new NPCImpl(world, type, location);
        NPCEntryImpl entry = new NPCEntryImpl(npc);
        npcMap.put(id, entry);
        return entry;
    }

    @Override
    public void delete(String id) {
        id = id.toUpperCase();
        if (!npcMap.containsKey(id)) return;
        npcMap.remove(id).getNpc().delete();
    }
}
