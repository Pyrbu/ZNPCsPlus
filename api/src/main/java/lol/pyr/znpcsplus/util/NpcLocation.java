package lol.pyr.znpcsplus.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

import java.util.Objects;

public class NpcLocation {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public NpcLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public NpcLocation(Location location) {
        this(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public double getX() {
        return this.x;
    }

    public int getBlockX() {
        return (int) getX();
    }

    public double getY() {
        return this.y;
    }

    public int getBlockY() {
        return (int) getY();
    }

    public double getZ() {
        return this.z;
    }

    public int getBlockZ() {
        return (int) getZ();
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public NpcLocation centered() {
        return new NpcLocation(Math.floor(x) + 0.5, y, Math.floor(z) + 0.5, yaw, pitch);
    }

    public Location toBukkitLocation(World world) {
        return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public NpcLocation withY(double y) {
        return new NpcLocation(x, y, z, yaw, pitch);
    }

    public NpcLocation withRotation(float yaw, float pitch) {
        return new NpcLocation(x, y, z, yaw, pitch);
    }

    private static final double _2PI = 2 * Math.PI;

    public NpcLocation lookingAt(Location loc) {
        return lookingAt(new NpcLocation(loc));
    }

    public NpcLocation lookingAt(NpcLocation loc) {
        final double x = loc.getX() - this.x;
        final double z = loc.getZ() - this.z;
        final double y = loc.getY() - this.y;

        if (x == 0 && z == 0) return new NpcLocation(this.x, this.y, this.z, this.yaw, y > 0 ? -90 : 90);

        double x2 = NumberConversions.square(x);
        double z2 = NumberConversions.square(z);
        double xz = Math.sqrt(x2 + z2);

        double theta = Math.atan2(-x, z);
        float yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);
        float pitch = (float) Math.toDegrees(Math.atan(-y / xz));

        return new NpcLocation(this.x, this.y, this.z, yaw, pitch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NpcLocation that = (NpcLocation) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0 &&
                Float.compare(that.yaw, yaw) == 0 &&
                Float.compare(that.pitch, pitch) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch);
    }
}
