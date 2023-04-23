package io.github.znetworkw.znpcservers.nms;

import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.utility.Utils;

public class NMSV9 extends NMSV8 {
    public int version() {
        return 9;
    }

    public void updateGlow(NPC npc, Object packet) throws ReflectiveOperationException {
        Object enumChatString = Reflections.ENUM_CHAT_TO_STRING_METHOD.get().invoke(npc.getGlowColor());
        if (Utils.BUKKIT_VERSION > 12) {
            Utils.setValue(packet, npc.getGlowColor(), Reflections.ENUM_CHAT_CLASS);
            Utils.setValue(packet, "c", Reflections.I_CHAT_BASE_COMPONENT_A_CONSTRUCTOR.get().newInstance(enumChatString));
        } else {
            Utils.setValue(packet, "g", Reflections.GET_ENUM_CHAT_ID_METHOD.get().invoke(npc.getGlowColor()));
            Utils.setValue(packet, "c", enumChatString);
        }
    }

    public boolean allowsGlowColor() {
        return true;
    }
}
