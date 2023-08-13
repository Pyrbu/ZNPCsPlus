package lol.pyr.znpcsplus.scheduling;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpigotScheduler extends TaskScheduler {
    public SpigotScheduler(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void schedulePlayerChat(Player player, String chat) {
        runSyncGlobal(() -> player.chat(chat));
    }

    @Override
    public void schedulePlayerCommand(Player player, String command) {
        runSyncGlobal(() -> Bukkit.dispatchCommand(player, command));
    }

    @Override
    public void runSyncGlobal(Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void runAsyncGlobal(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void runLaterAsync(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    @Override
    public void runDelayedTimerAsync(Runnable runnable, long delay, long interval) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
