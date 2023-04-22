package io.github.znetworkw.znpcservers.nms;

import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.utility.Utils;

public class NMSV18 extends NMSV17 {
    public int version() {
        return 18;
    }

    public void updateGlow(NPC npc, Object packet) throws ReflectiveOperationException {
        Utils.setValue(packet, "m", Reflections.ENUM_CHAT_FORMAT_FIND.get().invoke(null, npc.getNpcPojo().getGlowName()));
    }
}
