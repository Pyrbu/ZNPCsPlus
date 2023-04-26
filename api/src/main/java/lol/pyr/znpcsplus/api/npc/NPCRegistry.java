package lol.pyr.znpcsplus.api.npc;

import java.util.Collection;

public interface NPCRegistry {
    NPC get(String id);
    Collection<? extends NPC> all();
    NPC getByEntityId(int id);
    Collection<String> getRegisteredIds();
    void register(String id, NPC npc);
    void unregister(String id);
}