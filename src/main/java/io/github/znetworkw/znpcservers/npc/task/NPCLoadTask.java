package io.github.znetworkw.znpcservers.npc.task;

import lol.pyr.znpcsplus.ZNPCsPlus;
import io.github.znetworkw.znpcservers.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCLoadTask extends BukkitRunnable {
    private final NPC npc;

    private int tries = 0;

    public NPCLoadTask(NPC npc) {
        this.npc = npc;
        ZNPCsPlus.SCHEDULER.runTaskTimer(this, 40);
    }

    public void run() {
        if (this.tries++ > 10) {
            cancel();
            return;
        }
        World world = Bukkit.getWorld(this.npc.getNpcPojo().getLocation().getWorldName());
        if (world == null) return;
        cancel();
        this.npc.onLoad();
    }
}
