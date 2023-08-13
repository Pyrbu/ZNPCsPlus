package lol.pyr.znpcsplus.scheduling;

import lol.pyr.znpcsplus.reflection.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FoliaScheduler extends TaskScheduler {
    public FoliaScheduler(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void schedulePlayerChat(Player player, String chat) {
        try {
            Object scheduler = Reflections.FOLIA_GET_REGION_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_EXECUTE_REGION.get().invoke(scheduler, plugin, player.getLocation(), (Runnable) () -> player.chat(chat));
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void schedulePlayerCommand(Player player, String command) {
        try {
            Object scheduler = Reflections.FOLIA_GET_REGION_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_EXECUTE_REGION.get().invoke(scheduler, plugin, player.getLocation(), (Runnable) () -> Bukkit.dispatchCommand(player, command));
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void runSyncGlobal(Runnable runnable) {
        try {
            Object scheduler = Reflections.FOLIA_GET_GLOBAL_REGION_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_RUN_NOW_GLOBAL.get().invoke(scheduler, plugin, (Consumer<Object>) o -> runnable.run());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void runAsyncGlobal(Runnable runnable) {
        try {
            Object scheduler = Reflections.FOLIA_GET_ASYNC_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_RUN_NOW_ASYNC.get().invoke(scheduler, plugin, (Consumer<Object>) o -> runnable.run());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void runLaterAsync(Runnable runnable, long delay) {
        try {
            Object scheduler = Reflections.FOLIA_GET_ASYNC_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_RUN_DELAYED_ASYNC.get().invoke(scheduler, plugin, (Consumer<Object>) o -> runnable.run(), delay * 50, TimeUnit.MILLISECONDS);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void runDelayedTimerAsync(Runnable runnable, long delay, long interval) {
        try {
            Object scheduler = Reflections.FOLIA_GET_ASYNC_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_RUN_AT_FIXED_RATE_ASYNC.get().invoke(scheduler, plugin, (Consumer<Object>) o -> runnable.run(), delay * 50, interval * 50, TimeUnit.MILLISECONDS);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelAll() {
        try {
            Object asyncScheduler = Reflections.FOLIA_GET_ASYNC_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_CANCEL_ASYNC_TASKS.get().invoke(asyncScheduler, plugin);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        try {
            Object globalScheduler = Reflections.FOLIA_GET_GLOBAL_REGION_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_CANCEL_GLOBAL_TASKS.get().invoke(globalScheduler, plugin);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

