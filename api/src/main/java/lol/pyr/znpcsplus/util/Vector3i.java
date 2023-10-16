package lol.pyr.znpcsplus.util;

public class Vector3i {
    private final int x;
    private final int y;
    private final int z;

    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }

    public String toPrettyString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public static Vector3i fromString(String s) {
        String[] split = s.split(",");
        if (split.length < 3) {
            return null;
        } else {
            try {
                return new Vector3i(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            } catch (NumberFormatException var3) {
                return null;
            }
        }
    }
}
