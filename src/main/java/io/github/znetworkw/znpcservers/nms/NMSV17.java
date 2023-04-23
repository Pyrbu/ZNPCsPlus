package io.github.znetworkw.znpcservers.nms;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.Bukkit;

public class NMSV17 extends NMSV9 {
    public int version() {
        return 17;
    }

    public Object createPlayer(Object nmsWorld, GameProfile gameProfile) throws ReflectiveOperationException {
        return Reflections.PLAYER_CONSTRUCTOR_NEW.get().newInstance(Reflections.GET_SERVER_METHOD.get().invoke(Bukkit.getServer()), nmsWorld, gameProfile);
    }

    public void updateGlow(NPC npc, Object packet) throws ReflectiveOperationException {
        Utils.setValue(packet, "n", Reflections.ENUM_CHAT_FORMAT_FIND.get().invoke(null, npc.getNpcPojo().getGlowName()));
    }
}
