package lol.pyr.znpcsplus.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PapiUtil {
    private static boolean isSupported() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static String set(String str) {
        return set(null, str);
    }

    public static String set(Player player, String str) {
        return isSupported() ? PlaceholderAPI.setPlaceholders(player, str) : str;
    }
}
