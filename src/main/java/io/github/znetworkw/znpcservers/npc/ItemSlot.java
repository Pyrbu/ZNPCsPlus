package io.github.znetworkw.znpcservers.npc;

public enum ItemSlot {
    HELMET(5),
    CHESTPLATE(4),
    LEGGINGS(3),
    BOOTS(2),
    OFFHAND(1),
    HAND(0);

    private final int slot;

    private final int slotOld;

    ItemSlot(int slot) {
        this.slot = slot;
        this.slotOld = (slot == 0) ? 0 : (slot - 1);
    }

    public int getSlot() {
        return this.slot;
    }

    public int getSlotOld() {
        return this.slotOld;
    }
}
