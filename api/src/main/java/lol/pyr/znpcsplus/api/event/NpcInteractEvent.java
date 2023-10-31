package lol.pyr.znpcsplus.api.event;

import lol.pyr.znpcsplus.api.event.util.CancellableNpcEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event called when an NPC is interacted with by a player
 * Note: This event is async
 */
public class NpcInteractEvent extends CancellableNpcEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final InteractionType clickType;

    /**
     * @param player The player involved in the event
     * @param entry The NPC entry involved in the event
     * @param clickType The type of interaction. See {@link InteractionType}
     */
    public NpcInteractEvent(Player player, NpcEntry entry, InteractionType clickType) {
        super(player, entry);
        this.clickType = clickType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Returns the type of interaction. See {@link InteractionType}
     * @return The type of interaction
     */
    public InteractionType getClickType() {
        return clickType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
