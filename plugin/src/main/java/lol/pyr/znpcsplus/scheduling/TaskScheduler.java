package lol.pyr.znpcsplus.scheduling;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class TaskScheduler {
    protected final Plugin plugin;

    public TaskScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    public abstract void schedulePlayerChat(Player player, String message);
    public abstract void schedulePlayerCommand(Player player, String command);
    public abstract void runSyncGlobal(Runnable runnable);
    public abstract void runAsyncGlobal(Runnable runnable);
    public abstract void runLaterAsync(Runnable runnable, long delay);
    public abstract  void runDelayedTimerAsync(Runnable runnable, long delay, long interval);
    public abstract void cancelAll();
}
