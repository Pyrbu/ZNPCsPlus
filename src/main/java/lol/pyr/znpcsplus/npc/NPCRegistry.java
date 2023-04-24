package lol.pyr.znpcsplus.npc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NPCRegistry {
    private static final Map<String, NPC> npcMap = new HashMap<>();

    public static NPC get(String id) {
        return npcMap.get(id);
    }

    public static Collection<NPC> all() {
        return npcMap.values();
    }

    public static void register(String id, NPC npc) {
        npcMap.put(id, npc);
    }

    public static void unregister(String id) {
        npcMap.remove(id);
    }
}
