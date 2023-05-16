package lol.pyr.znpcsplus.skin.cache;

import org.bukkit.scheduler.BukkitRunnable;

public class SkinCacheCleanTask extends BukkitRunnable {
    private final SkinCache skinCache;

    public SkinCacheCleanTask(SkinCache skinCache) {
        this.skinCache = skinCache;
    }

    @Override
    public void run() {
        skinCache.cleanCache();
    }
}
