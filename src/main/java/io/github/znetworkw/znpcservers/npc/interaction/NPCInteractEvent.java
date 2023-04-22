package io.github.znetworkw.znpcservers.npc.interaction;

import io.github.znetworkw.znpcservers.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class NPCInteractEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final ClickType clickType;
    private final NPC npc;

    public NPCInteractEvent(Player player, ClickType clickType, NPC npc) {
        this.player = player;
        this.clickType = clickType;
        this.npc = npc;
    }

    public NPCInteractEvent(Player player, String clickType, NPC npc) {
        this(player, ClickType.forName(clickType), npc);
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Player getPlayer() {
        return this.player;
    }

    public NPC getNpc() {
        return this.npc;
    }

    public boolean isRightClick() {
        return (this.clickType == ClickType.RIGHT);
    }

    public boolean isLeftClick() {
        return (this.clickType == ClickType.LEFT);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}
