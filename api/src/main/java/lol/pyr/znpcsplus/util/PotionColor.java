package lol.pyr.znpcsplus.util;

public class PotionColor {
    private final int color;
    public static PotionColor DEFAULT = new PotionColor(0);

    public PotionColor(int color) {
        this.color = color;
    }

    public PotionColor(String color) {
        boolean hex = false;
        if (color.startsWith("#")) {
            color = color.substring(1);
            hex = true;
        }
        else if (color.startsWith("0x")) {
            color = color.substring(2);
            hex = true;
        }
        if (hex && color.length() != 6) throw new IllegalArgumentException("Hex color must be 6 characters long");
        this.color = Integer.parseInt(color);
    }

    public int getColor() {
        return color;
    }

    public String toString() {
        return String.valueOf(color);
    }

}
