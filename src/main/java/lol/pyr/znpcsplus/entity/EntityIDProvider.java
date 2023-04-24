package lol.pyr.znpcsplus.entity;

import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.utility.Utils;

public class EntityIDProvider {
    public static int reserve() {
        if (Utils.versionNewer(14)) return Reflections.ATOMIC_ENTITY_ID_FIELD.get().incrementAndGet();
        else {
            int id = Reflections.ENTITY_ID_MODIFIER.get();
            Reflections.ENTITY_ID_MODIFIER.set(id + 1);
            return id;
        }
    }
}
