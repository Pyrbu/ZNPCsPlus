package io.github.znetworkw.znpcservers.listeners;

import io.github.znetworkw.znpcservers.utility.inventory.ZInventoryHolder;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventoryPage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class InventoryListener implements Listener {
    public InventoryListener(Plugin serversNPC) {
        serversNPC.getServer().getPluginManager().registerEvents(this, serversNPC);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null) return;
        if (!(event.getInventory().getHolder() instanceof ZInventoryHolder holder)) return;
        event.setCancelled(true);

        ZInventoryPage page = holder.getzInventory().getPage();
        if (!page.containsItem(event.getRawSlot())) return;

        page.findItem(event.getRawSlot()).getInventoryCallback().onClick(event);
        player.updateInventory();
    }
}
