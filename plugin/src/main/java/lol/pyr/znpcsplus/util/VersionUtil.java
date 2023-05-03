package lol.pyr.znpcsplus.util;

import org.bukkit.Bukkit;

public final class VersionUtil {
    public static final int BUKKIT_VERSION;

    static {
        int version = 0;
        try {
            version = Integer.parseInt(getFormattedBukkitPackage());
        } catch (NumberFormatException ignored) {}
        BUKKIT_VERSION = version;
    }

    public static boolean isNewerThan(int version) {
        return (BUKKIT_VERSION >= version);
    }

    public static String getBukkitPackage() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static String getFormattedBukkitPackage() {
        String version = getBukkitPackage().replace("v", "").replace("R", "");
        return version.substring(2, version.length() - 2);
    }
}
