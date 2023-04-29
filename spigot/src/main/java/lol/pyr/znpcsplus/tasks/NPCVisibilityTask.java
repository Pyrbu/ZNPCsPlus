package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.config.Configs;
import lol.pyr.znpcsplus.npc.NPC;
import lol.pyr.znpcsplus.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class NPCVisibilityTask extends BukkitRunnable {
    public NPCVisibilityTask(Plugin plugin) {
        runTaskTimerAsynchronously(plugin, 60L, 10L);
    }

    public void run() {
        double distSq = NumberConversions.square(Configs.config().viewDistance());
        for (NPC npc : NPCRegistry.get().all()) for (Player player : Bukkit.getOnlinePlayers()) {
            boolean inRange = (player.getWorld() == npc.getWorld() && player.getLocation().distanceSquared(npc.getLocation().toBukkitLocation(npc.getWorld())) <= distSq);
            if (!inRange && npc.isShown(player)) npc.hide(player);
            if (inRange && !npc.isShown(player)) npc.show(player);
        }
    }
}
