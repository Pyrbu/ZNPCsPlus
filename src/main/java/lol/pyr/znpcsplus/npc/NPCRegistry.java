package lol.pyr.znpcsplus.npc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NPCRegistry {
    private static final Map<String, NPC> npcMap = new HashMap<>();

    public static NPC get(String id) {
        return npcMap.get(id);
    }

    public static Collection<NPC> all() {
        return Collections.unmodifiableSet(NPC._ALL_NPCS);
    }

    public static NPC getByEntityId(int id) {
        return all().stream().filter(npc -> npc.getEntity().getEntityId() == id).findFirst().orElse(null);
    }

    public static Collection<String> ids() {
        return Collections.unmodifiableSet(npcMap.keySet());
    }

    public static void register(String id, NPC npc) {
        npcMap.put(id, npc);
    }

    public static void unregister(String id) {
        npcMap.remove(id);
    }
}
