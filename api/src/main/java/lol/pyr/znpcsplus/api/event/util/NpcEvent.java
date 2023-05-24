package lol.pyr.znpcsplus.api.event.util;

import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class NpcEvent extends Event {
    private final NpcEntry entry;
    private final Player player;

    public NpcEvent(Player player, NpcEntry entry) {
        super(true); // All events are async since 95% of the plugin is async
        this.entry = entry;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public NpcEntry getEntry() {
        return entry;
    }

    public Npc getNpc() {
        return entry.getNpc();
    }
}
