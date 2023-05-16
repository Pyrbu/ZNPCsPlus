package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class NpcVisibilityTask extends BukkitRunnable {
    private final NpcRegistryImpl npcRegistry;
    private final ConfigManager configManager;

    public NpcVisibilityTask(NpcRegistryImpl npcRegistry, ConfigManager configManager) {
        this.npcRegistry = npcRegistry;
        this.configManager = configManager;
    }

    public void run() {
        double distSq = NumberConversions.square(configManager.getConfig().viewDistance());
        for (NpcEntryImpl entry : npcRegistry.all()) {
            if (!entry.isProcessed()) continue;
            NpcImpl npc = entry.getNpc();
            for (Player player : Bukkit.getOnlinePlayers()) {
                boolean inRange = (player.getWorld() == npc.getWorld() && player.getLocation().distanceSquared(npc.getBukkitLocation()) <= distSq);
                if (!inRange && npc.isShown(player)) npc.hide(player);
                if (inRange && !npc.isShown(player)) npc.show(player);
            }
        }
    }
}
