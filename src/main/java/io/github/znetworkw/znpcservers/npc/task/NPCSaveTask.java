package io.github.znetworkw.znpcservers.npc.task;

import io.github.znetworkw.znpcservers.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCSaveTask extends BukkitRunnable {
    public NPCSaveTask(Plugin serversNPC, int seconds) {
        runTaskTimer(serversNPC, 200L, seconds);
    }

    public void run() {
        Configuration.SAVE_CONFIGURATIONS.forEach(Configuration::save);
    }
}
