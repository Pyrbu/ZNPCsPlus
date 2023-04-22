package io.github.znetworkw.znpcservers.npc.task;

import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.user.ZUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCPositionTask extends BukkitRunnable {
    public NPCPositionTask(Plugin serversNPC) {
        runTaskTimerAsynchronously(serversNPC, 60L, 1L);
    }

    public void run() {
        for (NPC npc : NPC.all()) {
            if (npc.getNpcPath() != null) {
                npc.getNpcPath().handle();
            }
            else if (FunctionFactory.isTrue(npc, "look")) for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().equals(npc.getLocation().getWorld())) continue;
                ZUser user = ZUser.find(player);
                if (npc.getViewers().contains(user)) npc.lookAt(user, player.getLocation(), false);
            }
        }
    }
}
