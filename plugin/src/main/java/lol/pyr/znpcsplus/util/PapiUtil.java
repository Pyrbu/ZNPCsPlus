package lol.pyr.znpcsplus.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

    // Ugly workaround would be cool if a better solution existed
    public static Component set(LegacyComponentSerializer serializer, Player player, Component component) {
        if (!isSupported()) return component;
        return serializer.deserialize(set(player, serializer.serialize(component)));
    }
}
