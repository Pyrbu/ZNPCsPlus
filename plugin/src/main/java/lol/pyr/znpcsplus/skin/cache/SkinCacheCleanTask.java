package lol.pyr.znpcsplus.skin.cache;

import lol.pyr.znpcsplus.ZNpcsPlus;
import org.bukkit.scheduler.BukkitRunnable;

public class SkinCacheCleanTask extends BukkitRunnable {
    public SkinCacheCleanTask() {
        ZNpcsPlus.SCHEDULER.runDelayedTimerAsync(this, 1200, 1200);
    }

    @Override
    public void run() {
        SkinCache.cleanCache();
    }
}
