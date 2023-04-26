package io.github.znetworkw.znpcservers.utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class Utils {
    public static final int BUKKIT_VERSION;

    static {
        int version = 0;
        try {
            version = Integer.parseInt(getFormattedBukkitPackage());
        } catch (NumberFormatException ignored) {}
        BUKKIT_VERSION = version;
    }

    public static boolean versionNewer(int version) {
        return (BUKKIT_VERSION >= version);
    }

    public static String getBukkitPackage() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static String getFormattedBukkitPackage() {
        String version = getBukkitPackage().replace("v", "").replace("R", "");
        return version.substring(2, version.length() - 2);
    }

    public static String toColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
