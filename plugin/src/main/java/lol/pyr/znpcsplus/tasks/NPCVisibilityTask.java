package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.ZNPCsPlus;
import lol.pyr.znpcsplus.config.Configs;
import lol.pyr.znpcsplus.npc.NPCEntryImpl;
import lol.pyr.znpcsplus.npc.NPCImpl;
import lol.pyr.znpcsplus.npc.NPCRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class NPCVisibilityTask extends BukkitRunnable {
    public NPCVisibilityTask() {
        ZNPCsPlus.SCHEDULER.runDelayedTimerAsync(this, 60L, 10L);
    }

    public void run() {
        double distSq = NumberConversions.square(Configs.config().viewDistance());
        for (NPCEntryImpl entry : NPCRegistryImpl.get().all()) {
            if (!entry.isProcessed()) continue;
            NPCImpl npc = entry.getNpc();
            for (Player player : Bukkit.getOnlinePlayers()) {
                boolean inRange = (player.getWorld() == npc.getWorld() && player.getLocation().distanceSquared(npc.getLocation().toBukkitLocation(npc.getWorld())) <= distSq);
                if (!inRange && npc.isShown(player)) npc.hide(player);
                if (inRange && !npc.isShown(player)) npc.show(player);
            }
        }
    }
}
