package lol.pyr.znpcsplus.scheduling;

import lol.pyr.znpcsplus.reflection.Reflections;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FoliaScheduler extends TaskScheduler {
    public FoliaScheduler(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void runSync(Runnable runnable) {
        try {
            Object scheduler = Reflections.FOLIA_GET_GLOBAL_REGION_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_RUN_NOW_GLOBAL.get().invoke(scheduler, plugin, (Consumer<Object>) o -> runnable.run());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void runLaterAsync(Runnable runnable, long ticks) {
        try {
            Object scheduler = Reflections.FOLIA_GET_ASYNC_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_RUN_DELAYED.get().invoke(scheduler, plugin, (Consumer<Object>) o -> runnable.run(), ticks * 50, TimeUnit.MILLISECONDS);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void runDelayedTimerAsync(Runnable runnable, long delay, long ticks) {
        try {
            Object scheduler = Reflections.FOLIA_GET_ASYNC_SCHEDULER.get().invoke(null);
            Reflections.FOLIA_RUN_AT_FIXED_RATE.get().invoke(scheduler, plugin, (Consumer<Object>) o -> runnable.run(), delay * 50, ticks * 50, TimeUnit.MILLISECONDS);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

