package io.github.znetworkw.znpcservers.npc.packet;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.Bukkit;

public class PacketV17 extends PacketV16 {
    public int version() {
        return 17;
    }

    public Object getPlayerPacket(Object nmsWorld, GameProfile gameProfile) throws ReflectiveOperationException {
        return CacheRegistry.PLAYER_CONSTRUCTOR_NEW.load().newInstance(CacheRegistry.GET_SERVER_METHOD.load().invoke(Bukkit.getServer()), nmsWorld, gameProfile);
    }

    public void updateGlowPacket(NPC npc, Object packet) throws ReflectiveOperationException {
        Utils.setValue(packet, "n", CacheRegistry.ENUM_CHAT_FORMAT_FIND.load().invoke(null, npc.getNpcPojo().getGlowName()));
    }

    public Object getClickType(Object interactPacket) {
        return "INTERACT";
    }
}
