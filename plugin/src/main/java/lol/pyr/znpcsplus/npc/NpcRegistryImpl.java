package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.util.ZLocation;
import org.bukkit.World;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NpcRegistryImpl implements NpcRegistry {
    private final static NpcRegistryImpl registry = new NpcRegistryImpl();

    public static NpcRegistryImpl get() {
        return registry;
    }

    private NpcRegistryImpl() {
        if (registry != null) throw new UnsupportedOperationException("This class can only be instanciated once!");
    }

    private final Map<String, NpcEntryImpl> npcMap = new HashMap<>();

    public NpcEntryImpl get(String id) {
        return npcMap.get(id.toLowerCase());
    }

    public Collection<NpcEntryImpl> all() {
        return Collections.unmodifiableCollection(npcMap.values());
    }

    public NpcEntryImpl getByEntityId(int id) {
        return all().stream().filter(entry -> entry.getNpc().getEntity().getEntityId() == id).findFirst().orElse(null);
    }

    public Collection<String> ids() {
        return Collections.unmodifiableSet(npcMap.keySet());
    }

    public Collection<String> modifiableIds() {
        return Collections.unmodifiableSet(npcMap.entrySet().stream()
                .filter(entry -> entry.getValue().isAllowCommandModification())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet()));
    }

    public NpcEntryImpl create(String id, World world, NpcType type, ZLocation location) {
        return create(id, world, (NpcTypeImpl) type, location);
    }

    public NpcEntryImpl create(String id, World world, NpcTypeImpl type, ZLocation location) {
        id = id.toLowerCase();
        if (npcMap.containsKey(id)) throw new IllegalArgumentException("An npc with the id " + id + " already exists!");
        NpcImpl npc = new NpcImpl(world, type, location);
        NpcEntryImpl entry = new NpcEntryImpl(id, npc);
        npcMap.put(id, entry);
        return entry;
    }

    @Override
    public void delete(String id) {
        id = id.toLowerCase();
        if (!npcMap.containsKey(id)) return;
        npcMap.remove(id).getNpc().delete();
    }
}
