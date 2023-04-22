package io.github.znetworkw.znpcservers.user;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ZUser {
    private static final Map<UUID, ZUser> USER_MAP = new HashMap<>();

    private final Map<Integer, Long> lastClicked;

    private final List<EventService<?>> eventServices;

    private final UUID uuid;

    private final GameProfile gameProfile;

    private final Object playerConnection;

    private boolean hasPath = false;

    private long lastInteract = 0L;

    public ZUser(UUID uuid) {
        this.uuid = uuid;
        this.lastClicked = new HashMap<>();
        this.eventServices = new ArrayList<>();
        try {
            Object playerHandle = Reflections.GET_HANDLE_PLAYER_METHOD.get().invoke(toPlayer());
            this.gameProfile = (GameProfile) Reflections.GET_PROFILE_METHOD.get().invoke(playerHandle, new Object[0]);
            this.playerConnection = Reflections.PLAYER_CONNECTION_FIELD.get().get(playerHandle);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("can't create user for player " + uuid.toString(), e.getCause());
        }
    }

    public static ZUser find(UUID uuid) {
        return USER_MAP.computeIfAbsent(uuid, ZUser::new);
    }

    public static ZUser find(Player player) {
        return find(player.getUniqueId());
    }

    public static void unregister(Player player) {
        ZUser zUser = USER_MAP.get(player.getUniqueId());
        if (zUser == null)
            throw new IllegalStateException("can't find user " + player.getUniqueId());
        USER_MAP.remove(player.getUniqueId());
        NPC.all().stream()
                .filter(npc -> npc.getViewers().contains(zUser))
                .forEach(npc -> npc.delete(zUser));
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public Object getPlayerConnection() {
        return this.playerConnection;
    }

    public boolean isHasPath() {
        return this.hasPath;
    }

    public void setHasPath(boolean hasPath) {
        this.hasPath = hasPath;
    }

    public List<EventService<?>> getEventServices() {
        return this.eventServices;
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public long getLastInteract() {
        return lastInteract;
    }

    public Map<Integer, Long> getLastClicked() {
        return lastClicked;
    }

    public void updateLastInteract() {
        this.lastInteract = System.nanoTime();
    }
}
