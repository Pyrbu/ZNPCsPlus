package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class NpcHideListener implements Listener {
    private final NpcRegistryImpl registry;

    public NpcHideListener(NpcRegistryImpl registry) {
        this.registry = registry;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        registry.getProcessable().forEach(entry -> {
            if (entry.getNpc().isVisibleTo(event.getPlayer())) entry.getNpc().UNSAFE_removeViewer(event.getPlayer());
        });
    }
}
