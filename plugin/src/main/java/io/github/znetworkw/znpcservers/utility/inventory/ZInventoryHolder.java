package io.github.znetworkw.znpcservers.utility.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ZInventoryHolder implements InventoryHolder {
    private final ZInventory zInventory;

    public ZInventoryHolder(ZInventory zInventory) {
        this.zInventory = zInventory;
    }

    public ZInventory getzInventory() {
        return this.zInventory;
    }

    public Inventory getInventory() {
        return this.zInventory.getInventory();
    }
}
