package lol.pyr.znpcsplus.interaction;

import lol.pyr.znpcsplus.api.interaction.InteractionType;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class InteractionAction {
    private final UUID id;
    private final long delay;
    private final InteractionType interactionType;

    protected InteractionAction(long delay, InteractionType interactionType) {
        this.interactionType = interactionType;
        this.id = UUID.randomUUID();
        this.delay = delay;
    }

    public UUID getUuid() {
        return id;
    }

    public long getCooldown() {
        return delay;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public abstract void run(Player player);
}
