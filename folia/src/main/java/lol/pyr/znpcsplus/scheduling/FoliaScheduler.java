package lol.pyr.znpcsplus.scheduling;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class FoliaScheduler extends TaskScheduler {
    public FoliaScheduler(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getAsyncScheduler().runNow(plugin, task -> runnable.run());
    }

    @Override
    public void runLaterAsync(Runnable runnable, long ticks) {
        Bukkit.getAsyncScheduler().runDelayed(plugin, task -> runnable.run(), ticks * 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void runDelayedTimerAsync(Runnable runnable, long delay, long ticks) {
        Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task -> runnable.run(), delay * 50, ticks * 50, TimeUnit.MILLISECONDS);
    }
}
