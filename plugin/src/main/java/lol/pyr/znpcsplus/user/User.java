package lol.pyr.znpcsplus.user;

import lol.pyr.znpcsplus.interaction.InteractionAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
    private final UUID uuid;
    private long lastNpcInteraction;
    private final Map<UUID, Long> actionCooldownMap = new HashMap<>();

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean canInteract() {
        if (System.currentTimeMillis() - lastNpcInteraction > 100L) {
            lastNpcInteraction = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean actionCooldownCheck(InteractionAction action) {
        UUID id = action.getUuid();
        if (System.currentTimeMillis() - actionCooldownMap.getOrDefault(id, 0L) >= action.getCooldown()) {
            actionCooldownMap.put(id, System.currentTimeMillis());
            return true;
        }
        return false;
    }
}
