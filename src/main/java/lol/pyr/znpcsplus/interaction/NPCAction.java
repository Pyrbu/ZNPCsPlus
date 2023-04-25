package lol.pyr.znpcsplus.interaction;

import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class NPCAction {
    private final UUID id;
    private final long delay;

    protected NPCAction(long delay) {
        this.id = UUID.randomUUID();
        this.delay = delay;
    }

    public UUID getUuid() {
        return id;
    }

    public long getCooldown() {
        return delay;
    }

    public abstract void run(Player player);
}
