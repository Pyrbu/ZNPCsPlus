package io.github.znetworkw.znpcservers.utility.inventory;

import io.github.znetworkw.znpcservers.utility.itemstack.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class ZInventoryPage {
    private final ZInventory zInventory;

    private final String pageName;

    private final int rows;

    private final List<ZInventoryItem> inventoryItems;

    public ZInventoryPage(ZInventory zInventory, String inventoryName, int rows) {
        this.zInventory = zInventory;
        this.pageName = inventoryName;
        this.rows = rows * 9;
        this.inventoryItems = new ArrayList<>();
        if (zInventory.getInventory() != null) {
            ZInventoryPage zInventoryPage = zInventory.getPage();
            addItem(ItemStackBuilder.forMaterial(Material.ARROW)
                    .setName(ChatColor.GREEN + "Go back")
                    .setLore(ChatColor.GRAY + "click here...").build(), this.rows - 9, true, event -> {
                zInventory.setCurrentPage(zInventoryPage);
                openInventory();
            });
        }
        zInventory.setCurrentPage(this);
    }

    public ZInventory getInventory() {
        return this.zInventory;
    }

    public String getPageName() {
        return this.pageName;
    }

    public int getRows() {
        return this.rows;
    }

    public List<ZInventoryItem> getInventoryItems() {
        return this.inventoryItems;
    }

    public boolean containsItem(int slot) {
        return this.inventoryItems.stream().anyMatch(zInventoryItem -> (zInventoryItem.getSlot() == slot));
    }

    public ZInventoryItem findItem(int slot) {
        return this.inventoryItems.stream()
                .filter(zInventoryItem -> (zInventoryItem.getSlot() == slot))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find item for slot " + slot));
    }

    public void addItem(ItemStack itemStack, int slot, boolean isDefault, ZInventoryCallback callback) {
        this.inventoryItems.add(new ZInventoryItem(itemStack, slot, isDefault, callback));
    }

    public void addItem(ItemStack itemStack, int slot, ZInventoryCallback callback) {
        addItem(itemStack, slot, false, callback);
    }

    public Player getPlayer() {
        return this.zInventory.getPlayer();
    }

    public void openInventory() {
        this.zInventory.getPlayer().openInventory(this.zInventory.build());
    }

    public abstract void update();
}
