package io.github.znetworkw.znpcservers.npc.event;

public enum ClickType {
    RIGHT, LEFT, DEFAULT;

    public static ClickType forName(String clickName) {
        if (clickName.startsWith("INTERACT"))
            return RIGHT;
        if (clickName.startsWith("ATTACK"))
            return LEFT;
        return DEFAULT;
    }
}
