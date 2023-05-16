package lol.pyr.znpcsplus.util;

import lol.pyr.znpcsplus.reflection.Reflections;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationTargetException;

public class FoliaUtil {
    private static final Boolean FOLIA = isFolia();
    public static boolean isFolia() {
        if (FOLIA != null) return FOLIA;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void teleport(Entity entity, Location location) {
        if (!isFolia()) entity.teleport(location);
        else try {
            Reflections.FOLIA_TELEPORT_ASYNC.get().invoke(entity, location);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("Error while teleporting entity:");
            e.printStackTrace();
        }
    }
}
