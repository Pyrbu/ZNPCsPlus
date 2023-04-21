package io.github.znetworkw.znpcservers.npc.packet;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import org.bukkit.Bukkit;

public class PacketV19 extends PacketV18 {
    public int version() {
        return 19;
    }

    public Object getPlayerPacket(Object nmsWorld, GameProfile gameProfile) throws ReflectiveOperationException {
        try {
            return Reflections.PLAYER_CONSTRUCTOR_NEW_1.get(true).newInstance(Reflections.GET_SERVER_METHOD.get().invoke(Bukkit.getServer()), nmsWorld, gameProfile, null);
        } catch (Throwable e) {
            return Reflections.PLAYER_CONSTRUCTOR_NEW_2.get().newInstance(Reflections.GET_SERVER_METHOD.get().invoke(Bukkit.getServer()), nmsWorld, gameProfile);
        }
    }
}
