package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.api.npc.NPC;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NPCRegistry implements lol.pyr.znpcsplus.api.NPCRegistry {
    private final static NPCRegistry registry = new NPCRegistry();

    public static NPCRegistry get() {
        return registry;
    }

    private final Map<String, NPC> npcMap = new HashMap<>();

    private NPCRegistry() {
        if (registry != null) throw new UnsupportedOperationException("This class can only be instanciated once!");
    }

    public NPC get(String id) {
        return npcMap.get(id);
    }

    public Collection<lol.pyr.znpcsplus.npc.NPC> all() {
        return Collections.unmodifiableSet(lol.pyr.znpcsplus.npc.NPC._ALL_NPCS);
    }

    public lol.pyr.znpcsplus.npc.NPC getByEntityId(int id) {
        return all().stream().filter(npc -> npc.getEntity().getEntityId() == id).findFirst().orElse(null);
    }

    public Collection<String> getRegisteredIds() {
        return Collections.unmodifiableSet(npcMap.keySet());
    }

    public void register(String id, NPC npc) {
        npcMap.put(id, npc);
    }

    public void unregister(String id) {
        npcMap.remove(id);
    }
}
