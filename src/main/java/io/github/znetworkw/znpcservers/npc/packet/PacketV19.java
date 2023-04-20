package io.github.znetworkw.znpcservers.npc.packet;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.reflection.ReflectionCache;
import org.bukkit.Bukkit;

public class PacketV19 extends PacketV18 {
    public int version() {
        return 19;
    }

    public Object getPlayerPacket(Object nmsWorld, GameProfile gameProfile) throws ReflectiveOperationException {
        try {
            return ReflectionCache.PLAYER_CONSTRUCTOR_NEW_1.load(true).newInstance(ReflectionCache.GET_SERVER_METHOD.load().invoke(Bukkit.getServer()), nmsWorld, gameProfile, null);
        } catch (Throwable e) {
            return ReflectionCache.PLAYER_CONSTRUCTOR_NEW_2.load().newInstance(ReflectionCache.GET_SERVER_METHOD.load().invoke(Bukkit.getServer()), nmsWorld, gameProfile);
        }
    }
}
