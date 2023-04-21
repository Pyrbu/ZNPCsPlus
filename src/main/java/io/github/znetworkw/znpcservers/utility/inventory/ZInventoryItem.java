package io.github.znetworkw.znpcservers.utility.inventory;

import org.bukkit.inventory.ItemStack;

public class ZInventoryItem {
    private final ItemStack itemStack;
    private final int slot;
    private final boolean isDefault;
    private final ZInventoryCallback clickCallback;

    public ZInventoryItem(ItemStack itemStack, int slot, boolean isDefault, ZInventoryCallback zInventoryCallback) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.isDefault = isDefault;
        this.clickCallback = zInventoryCallback;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public ZInventoryCallback getInventoryCallback() {
        return this.clickCallback;
    }
}
