package lol.pyr.znpcsplus.util;

public enum MooshroomVariant {
    RED,
    BROWN;

    public static String getVariantName(MooshroomVariant variant) {
        return variant.name().toLowerCase();
    }
}
