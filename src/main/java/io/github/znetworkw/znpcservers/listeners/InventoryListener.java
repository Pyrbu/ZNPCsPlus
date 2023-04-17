package io.github.znetworkw.znpcservers.listeners;

import io.github.znetworkw.znpcservers.utility.inventory.ZInventory;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventoryHolder;
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
        if (!(event.getWhoClicked() instanceof Player))
            return;
        if (event.getCurrentItem() == null)
            return;
        if (!(event.getInventory().getHolder() instanceof ZInventoryHolder))
            return;
        event.setCancelled(true);
        ZInventory zInventory = ((ZInventoryHolder) event.getInventory().getHolder()).getzInventory();
        if (!zInventory.getPage().containsItem(event.getRawSlot()))
            return;
        zInventory.getPage().findItem(event.getRawSlot()).getInventoryCallback().onClick(event);
        ((Player) event.getWhoClicked()).updateInventory();
    }
}
