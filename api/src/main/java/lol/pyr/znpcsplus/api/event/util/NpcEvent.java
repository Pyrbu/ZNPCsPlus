package lol.pyr.znpcsplus.api.event.util;

import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Base class for all NPC events
 */
public abstract class NpcEvent extends Event {
    private final NpcEntry entry;
    private final Player player;

    /**
     * @param player The player involved in the event
     * @param entry The NPC entry involved in the event
     */
    public NpcEvent(Player player, NpcEntry entry) {
        super(true); // All events are async since 99% of the plugin is async
        this.entry = entry;
        this.player = player;
    }

    /**
     * Returns the player involved in the event
     * @return The player involved in the event
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the NPC entry involved in the event
     * @return The NPC entry involved in the event
     */
    public NpcEntry getEntry() {
        return entry;
    }

    /**
     * Returns the NPC involved in the event
     * @return The NPC involved in the event
     */
    public Npc getNpc() {
        return entry.getNpc();
    }
}
