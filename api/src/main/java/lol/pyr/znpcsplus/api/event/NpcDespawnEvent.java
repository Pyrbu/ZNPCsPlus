package lol.pyr.znpcsplus.api.event;

import lol.pyr.znpcsplus.api.event.util.CancellableNpcEvent;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NpcDespawnEvent extends CancellableNpcEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public NpcDespawnEvent(Player player, NpcEntry entry) {
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
