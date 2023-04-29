package lol.pyr.znpcsplus.scheduling;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SpigotScheduler extends TaskScheduler {
    public SpigotScheduler(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void runLaterAsync(Runnable runnable, long ticks) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, ticks);
    }
}
