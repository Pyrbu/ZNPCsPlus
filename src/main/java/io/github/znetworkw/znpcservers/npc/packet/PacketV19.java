package io.github.znetworkw.znpcservers.npc.packet;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;

public class PacketV19 extends PacketV18 {
    public int version() {
        return 19;
    }

    public Object getPlayerPacket(Object nmsWorld, GameProfile gameProfile) throws ReflectiveOperationException {
        try {
            return ((Constructor) CacheRegistry.PLAYER_CONSTRUCTOR_NEW_1.load()).newInstance(new Object[]{CacheRegistry.GET_SERVER_METHOD
                    .load().invoke(Bukkit.getServer()), nmsWorld, gameProfile, null});
        } catch (Throwable e) {
            return ((Constructor) CacheRegistry.PLAYER_CONSTRUCTOR_NEW_2.load()).newInstance(new Object[]{CacheRegistry.GET_SERVER_METHOD
                    .load().invoke(Bukkit.getServer()), nmsWorld, gameProfile});
        }
    }
}
