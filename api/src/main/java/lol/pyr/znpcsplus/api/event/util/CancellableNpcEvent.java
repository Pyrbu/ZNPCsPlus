package lol.pyr.znpcsplus.api.event.util;

import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Base class for all NPC events that can be cancelled
 */
public abstract class CancellableNpcEvent extends NpcEvent implements Cancellable {
    private boolean cancelled = false;

    /**
     * @param player The player involved in the event
     * @param entry The NPC entry involved in the event
     */
    public CancellableNpcEvent(Player player, NpcEntry entry) {
        super(player, entry);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
