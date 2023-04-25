package lol.pyr.znpcsplus.user;

import io.github.znetworkw.znpcservers.user.EventService;
import lol.pyr.znpcsplus.interaction.NPCAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class User {
    private final static Map<UUID, User> USER_MAP = new HashMap<>();

    public static User get(Player player) {
        return get(player.getUniqueId());
    }

    public static User get(UUID uuid) {
        return USER_MAP.computeIfAbsent(uuid, User::new);
    }

    public static void remove(Player player) {
        remove(player.getUniqueId());
    }

    public static void remove(UUID uuid) {
        USER_MAP.remove(uuid);
    }

    private final UUID uuid;
    private long lastNPCInteraction;
    private final Map<UUID, Long> actionCooldownMap = new HashMap<>();
    private final List<EventService<?>> eventServices = new ArrayList<>();

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean canInteract() {
        if (System.currentTimeMillis() - lastNPCInteraction > 100L) {
            lastNPCInteraction = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public List<EventService<?>> getEventServices() {
        return eventServices;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean actionCooldownCheck(NPCAction action) {
        UUID id = action.getUuid();
        if (System.currentTimeMillis() - actionCooldownMap.getOrDefault(id, 0L) >= action.getCooldown()) {
            actionCooldownMap.put(id, System.currentTimeMillis());
            return true;
        }
        return false;
    }
}
