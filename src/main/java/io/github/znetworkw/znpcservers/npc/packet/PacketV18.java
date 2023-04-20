package io.github.znetworkw.znpcservers.npc.packet;

import io.github.znetworkw.znpcservers.reflection.ReflectionCache;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.utility.Utils;

public class PacketV18 extends PacketV17 {
    public int version() {
        return 18;
    }

    public void updateGlowPacket(NPC npc, Object packet) throws ReflectiveOperationException {
        Utils.setValue(packet, "m", ReflectionCache.ENUM_CHAT_FORMAT_FIND.get().invoke(null, npc.getNpcPojo().getGlowName()));
    }
}
