package lol.pyr.znpcsplus.util;

public class PotionColor {

    private final String color;

    public static PotionColor DEFAULT = new PotionColor("0");

    public PotionColor(String color) {
        this.color = color;
    }

    public int getColor() {
        if (color.startsWith("#")) {
            if (color.length() != 7)
                throw new IllegalArgumentException("Hex color must be 6 characters long");
            return Integer.parseInt(color.substring(1), 16);
        } else if (color.startsWith("0x")) {
            if (color.length() != 8)
                throw new IllegalArgumentException("Hex color must be 6 characters long");
            return Integer.parseInt(color.substring(2), 16);
        } else {
            return Integer.parseInt(color);
        }
    }

    public String toString() {
        return String.valueOf(color);
    }

}
