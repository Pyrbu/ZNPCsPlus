package lol.pyr.znpcsplus.util;

public class Vector3f {
    private final float x;
    private final float y;
    private final float z;

    public Vector3f() {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(String s) {
        String[] split = s.split(",");
        this.x = Float.parseFloat(split[0]);
        this.y = Float.parseFloat(split[1]);
        this.z = Float.parseFloat(split[2]);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }

    public static Vector3f zero() {
        return new Vector3f();
    }
}
