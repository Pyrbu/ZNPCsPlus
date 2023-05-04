package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.config.Configs;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class NpcVisibilityTask extends BukkitRunnable {
    public NpcVisibilityTask() {
        ZNpcsPlus.SCHEDULER.runDelayedTimerAsync(this, 60L, 10L);
    }

    public void run() {
        double distSq = NumberConversions.square(Configs.config().viewDistance());
        for (NpcEntryImpl entry : NpcRegistryImpl.get().all()) {
            if (!entry.isProcessed()) continue;
            NpcImpl npc = entry.getNpc();
            for (Player player : Bukkit.getOnlinePlayers()) {
                boolean inRange = (player.getWorld() == npc.getWorld() && player.getLocation().distanceSquared(npc.getLocation().toBukkitLocation(npc.getWorld())) <= distSq);
                if (!inRange && npc.isShown(player)) npc.hide(player);
                if (inRange && !npc.isShown(player)) npc.show(player);
            }
        }
    }
}
