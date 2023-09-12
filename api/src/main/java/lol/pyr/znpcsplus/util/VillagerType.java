package lol.pyr.znpcsplus.util;

public enum VillagerType {
    DESERT(0),
    JUNGLE(1),
    PLAINS(2),
    SAVANNA(3),
    SNOW(4),
    SWAMP(5),
    TAIGA(6);
    private final int id;

    VillagerType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
