package lol.pyr.znpcsplus.api.event;

import lol.pyr.znpcsplus.api.event.util.CancellableNpcEvent;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event called when an NPC is spawned for a player
 * Note: This event is async
 */
public class NpcSpawnEvent extends CancellableNpcEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * @param player The player involved in the event
     * @param entry The NPC entry involved in the event
     */
    public NpcSpawnEvent(Player player, NpcEntry entry) {
        super(player, entry);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
