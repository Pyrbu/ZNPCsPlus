package lol.pyr.znpcsplus.scheduling;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SpigotScheduler extends TaskScheduler {
    public SpigotScheduler(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void runLaterAsync(Runnable runnable, long ticks) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, ticks);
    }

    @Override
    public void runDelayedTimerAsync(Runnable runnable, long delay, long ticks) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, ticks);
    }
}
