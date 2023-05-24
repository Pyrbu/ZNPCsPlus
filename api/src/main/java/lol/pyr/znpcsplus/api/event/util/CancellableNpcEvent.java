package lol.pyr.znpcsplus.api.event.util;

import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public abstract class CancellableNpcEvent extends NpcEvent implements Cancellable {
    private boolean cancelled = false;

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
