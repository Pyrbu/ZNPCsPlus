package lol.pyr.znpcsplus.util;

public enum CatVariant {
    TABBY(0),
    BLACK(1),
    RED(2),
    SIAMESE(3),
    BRITISH_SHORTHAIR(4),
    CALICO(5),
    PERSIAN(6),
    RAGDOLL(7),
    WHITE(8),
    JELLIE(9),
    ALL_BLACK(10);

    private final int id;

    CatVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
