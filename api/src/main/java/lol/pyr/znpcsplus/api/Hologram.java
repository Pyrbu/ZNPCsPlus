package lol.pyr.znpcsplus.api;

import net.kyori.adventure.text.Component;

public interface Hologram {
    void addLine(Component line);
    Component getLine(int index);
    void removeLine(int index);
    void clearLines();
    void insertLine(int index, Component line);
}
