package lol.pyr.znpcsplus.scheduling;

import org.bukkit.plugin.Plugin;

public abstract class TaskScheduler {
    protected final Plugin plugin;

    public TaskScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    public abstract void runSync(Runnable runnable);

    public abstract void runLaterAsync(Runnable runnable, long ticks);

    public abstract  void runDelayedTimerAsync(Runnable runnable, long delay, long ticks);

    public abstract void cancelAll();
}
