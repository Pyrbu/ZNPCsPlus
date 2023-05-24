package lol.pyr.znpcsplus.api.event;

import lol.pyr.znpcsplus.api.event.util.CancellableNpcEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NpcInteractEvent extends CancellableNpcEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final InteractionType clickType;

    public NpcInteractEvent(Player player, NpcEntry entry, InteractionType clickType) {
        super(player, entry);
        this.clickType = clickType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public InteractionType getClickType() {
        return clickType;
    }
}
