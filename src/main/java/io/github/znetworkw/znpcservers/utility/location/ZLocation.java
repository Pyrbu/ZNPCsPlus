package io.github.znetworkw.znpcservers.utility.location;

import com.github.retrooper.packetevents.util.Vector3d;
import com.google.common.base.Preconditions;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.lang.reflect.Type;

public class ZLocation {
    public static final ZLocationSerializer SERIALIZER = new ZLocationSerializer();
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public ZLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public ZLocation(Location location) {
        this(Preconditions.checkNotNull(location.getWorld()).getName(), location
                .getX(), location
                .getY(), location
                .getZ(), location
                .getYaw(), location
                .getPitch());
    }

    public String getWorldName() {
        return this.worldName;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Location toBukkitLocation() {
        return new Location(getWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    private static final double _2PI = 2 * Math.PI;

    public Location pointingTo(Location loc) {
        return pointingTo(new ZLocation(loc)).toBukkitLocation();
    }

    public ZLocation pointingTo(ZLocation loc) {
        /*
         * Sin = Opp / Hyp
         * Cos = Adj / Hyp
         * Tan = Opp / Adj
         *
         * x = -Opp
         * z = Adj
         */
        final double x = loc.getX() - this.x;
        final double z = loc.getZ() - this.z;
        final double y = loc.getY() - this.y;

        if (x == 0 && z == 0) {
            return new ZLocation(worldName, this.x, this.y, this.z, this.yaw, y > 0 ? -90 : 90);
        }

        double x2 = NumberConversions.square(x);
        double z2 = NumberConversions.square(z);
        double xz = Math.sqrt(x2 + z2);

        double theta = Math.atan2(-x, z);
        float yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);
        float pitch = (float) Math.toDegrees(Math.atan(-y / xz));

        return new ZLocation(worldName, this.x, this.y, this.z, yaw, pitch);
    }

    static class ZLocationSerializer implements JsonSerializer<ZLocation>, JsonDeserializer<ZLocation> {
        public JsonElement serialize(ZLocation src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("world", src.getWorldName());
            jsonObject.addProperty("x", src.getX());
            jsonObject.addProperty("y", src.getY());
            jsonObject.addProperty("z", src.getZ());
            jsonObject.addProperty("yaw", src.getYaw());
            jsonObject.addProperty("pitch", src.getPitch());
            return jsonObject;
        }

        public ZLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new ZLocation(jsonObject.get("world").getAsString(), jsonObject
                    .get("x").getAsDouble(), jsonObject
                    .get("y").getAsDouble(), jsonObject
                    .get("z").getAsDouble(), jsonObject
                    .get("yaw").getAsFloat(), jsonObject
                    .get("pitch").getAsFloat());
        }
    }
}
