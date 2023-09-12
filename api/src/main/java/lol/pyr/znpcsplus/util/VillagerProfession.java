package lol.pyr.znpcsplus.util;

public enum VillagerProfession {
    NONE(0),
    ARMORER(1),
    BUTCHER(2),
    CARTOGRAPHER(3),
    CLERIC(4),
    FARMER(5),
    FISHERMAN(6),
    FLETCHER(7),
    LEATHER_WORKER(8),
    LIBRARIAN(9),
    MASON(10),
    NITWIT(11),
    SHEPHERD(12),
    TOOL_SMITH(13),
    WEAPON_SMITH(14);

    private final int id;

    VillagerProfession(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
