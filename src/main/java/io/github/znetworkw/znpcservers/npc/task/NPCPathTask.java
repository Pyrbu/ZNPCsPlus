package io.github.znetworkw.znpcservers.npc.task;

import io.github.znetworkw.znpcservers.npc.NPC;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCPathTask extends BukkitRunnable {
    public NPCPathTask(Plugin serversNPC) {
        runTaskTimerAsynchronously(serversNPC, 60L, 1L);
    }

    public void run() {
        for (NPC npc : NPC.all()) if (npc.getNpcPath() != null) npc.getNpcPath().handle();
    }
}
