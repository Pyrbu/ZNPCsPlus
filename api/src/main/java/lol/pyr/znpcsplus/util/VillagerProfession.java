package lol.pyr.znpcsplus.util;

public enum VillagerProfession {
    NONE(0),
    ARMORER(3),
    BUTCHER(4),
    CARTOGRAPHER(1),
    CLERIC(2),
    FARMER(0),
    FISHERMAN(0),
    FLETCHER(0),
    LEATHER_WORKER(4),
    LIBRARIAN(1),
    MASON(-1),
    NITWIT(5),
    SHEPHERD(0),
    TOOL_SMITH(3),
    WEAPON_SMITH(3);

    private final int legacyId;

    VillagerProfession(int legacyId) {
        this.legacyId = legacyId;
    }

    public int getLegacyId() {
        return legacyId;
    }
}
