package lol.pyr.znpcsplus.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {
    private final Map<UUID, User> userMap = new HashMap<>();

    public UserManager() {
        Bukkit.getOnlinePlayers().forEach(this::get);
    }

    public User get(Player player) {
        return get(player.getUniqueId());
    }

    public User get(UUID uuid) {
        return userMap.computeIfAbsent(uuid, User::new);
    }

    public void remove(Player player) {
        remove(player.getUniqueId());
    }

    public void remove(UUID uuid) {
        userMap.remove(uuid);
    }

    public void shutdown() {
        Bukkit.getOnlinePlayers().forEach(this::remove);
    }
}
