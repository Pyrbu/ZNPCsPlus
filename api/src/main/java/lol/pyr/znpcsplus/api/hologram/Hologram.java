package lol.pyr.znpcsplus.api.hologram;

public interface Hologram {
    void addLine(String line);
    String getLine(int index);
    void removeLine(int index);
    void clearLines();
    void insertLine(int index, String line);
}
