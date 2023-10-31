package lol.pyr.znpcsplus.api.interaction;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Base class for all NPC interactions
 */
public abstract class InteractionAction {
    /**
     * The unique ID of this interaction
     */
    private final UUID id;

    /**
     * The cooldown of this interaction in seconds
     */
    private final long cooldown;

    /**
     * The delay of this interaction in ticks
     */
    private final long delay;

    /**
     * The type of this interaction
     */
    private final InteractionType interactionType;

    /**
     * @param cooldown The cooldown of this interaction in seconds
     * @param delay The delay of this interaction in ticks
     * @param interactionType The type of this interaction
     */
    protected InteractionAction(long cooldown, long delay, InteractionType interactionType) {
        this.interactionType = interactionType;
        this.id = UUID.randomUUID();
        this.cooldown = cooldown;
        this.delay = delay;
    }

    /**
     * @return The unique ID of this interaction
     */
    public UUID getUuid() {
        return id;
    }

    /**
     * @return The cooldown of this interaction in seconds
     */
    public long getCooldown() {
        return cooldown;
    }

    /**
     * @return The delay of this interaction in ticks
     */
    public long getDelay() {
        return delay;
    }

    /**
     * @return The type of this interaction
     */
    public InteractionType getInteractionType() {
        return interactionType;
    }

    /**
     * Runs this interaction
     * @param player The player that triggered this interaction
     */
    public abstract void run(Player player);
}
