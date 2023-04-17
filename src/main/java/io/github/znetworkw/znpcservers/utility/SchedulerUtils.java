package io.github.znetworkw.znpcservers.utility;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SchedulerUtils {
    private final Plugin plugin;

    public SchedulerUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runTaskTimer(BukkitRunnable bukkitRunnable, int delay) {
        return runTaskTimer(bukkitRunnable, delay, delay);
    }

    public BukkitTask runTaskTimer(BukkitRunnable bukkitRunnable, int delay, int continuousDelay) {
        return bukkitRunnable.runTaskTimer(this.plugin, delay, continuousDelay);
    }

    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, int delay, int continuousDelay) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, delay, continuousDelay);
    }

    public void scheduleSyncDelayedTask(Runnable runnable, int delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, runnable, delay);
    }

    public BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask(this.plugin, runnable);
    }
}
