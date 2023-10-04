package lol.pyr.znpcsplus.util;

public enum RabbitType {
    BROWN(0),
    WHITE(1),
    BLACK(2),
    BLACK_AND_WHITE(3),
    GOLD(4),
    SALT_AND_PEPPER(5),
    THE_KILLER_BUNNY(99),
    TOAST(100);

    private final int id;

    RabbitType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
