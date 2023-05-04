package lol.pyr.znpcsplus.interaction;

import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class NpcAction {
    private final UUID id;
    private final long delay;
    protected final String argument;

    protected NpcAction(long delay, String argument) {
        this.id = UUID.randomUUID();
        this.delay = delay;
        this.argument = argument;
    }

    public UUID getUuid() {
        return id;
    }

    public long getCooldown() {
        return delay;
    }

    public abstract void run(Player player);
}
