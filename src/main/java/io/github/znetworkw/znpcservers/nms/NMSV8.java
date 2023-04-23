package io.github.znetworkw.znpcservers.nms;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;

public class NMSV8 implements NMS {
    public int version() {
        return 8;
    }

    public Object createPlayer(Object nmsWorld, GameProfile gameProfile) throws ReflectiveOperationException {
        Constructor<?> constructor = (Utils.BUKKIT_VERSION > 13) ? Reflections.PLAYER_INTERACT_MANAGER_NEW_CONSTRUCTOR.get() : Reflections.PLAYER_INTERACT_MANAGER_OLD_CONSTRUCTOR.get();
        return Reflections.PLAYER_CONSTRUCTOR_OLD.get().newInstance(Reflections.GET_SERVER_METHOD
                .get().invoke(Bukkit.getServer()), nmsWorld, gameProfile, constructor.newInstance(nmsWorld));
    }

    public Object createMetadataPacket(int entityId, Object nmsEntity) throws ReflectiveOperationException {
        Object dataWatcher = Reflections.GET_DATA_WATCHER_METHOD.get().invoke(nmsEntity);
        try {
            return Reflections.PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR.get().newInstance(entityId, dataWatcher, true);
        } catch (Exception e2) {
            return Reflections.PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR_V1.get().newInstance(entityId, Reflections.GET_DATAWATCHER_B_LIST.get().invoke(dataWatcher));
        }
    }

    public Object createArmorStandSpawnPacket(Object armorStand) throws ReflectiveOperationException {
        return Reflections.PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR.get().newInstance(armorStand);
    }

    public void updateGlow(NPC npc, Object packet) throws ReflectiveOperationException {
        throw new IllegalStateException("Glow color is not supported for 1.8 version.");
    }

    public boolean allowsGlowColor() {
        return false;
    }
}
