package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.api.event.NpcDespawnEvent;
import lol.pyr.znpcsplus.api.event.NpcSpawnEvent;
import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.npc.ModeledNpcImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class NpcProcessorTask extends BukkitRunnable {
    private final NpcRegistryImpl npcRegistry;
    private final ConfigManager configManager;
    private final EntityPropertyRegistryImpl propertyRegistry;

    public NpcProcessorTask(NpcRegistryImpl npcRegistry, ConfigManager configManager, EntityPropertyRegistryImpl propertyRegistry) {
        this.npcRegistry = npcRegistry;
        this.configManager = configManager;
        this.propertyRegistry = propertyRegistry;
    }

    public void run() {
        double distSq = NumberConversions.square(configManager.getConfig().viewDistance());
        double lookPropertyDistSq = NumberConversions.square(configManager.getConfig().lookPropertyDistance());
        EntityPropertyImpl<Boolean> lookProperty = propertyRegistry.getByName("look", Boolean.class);
        for (NpcEntryImpl entry : npcRegistry.getProcessable()) {
            Npc npc = entry.getNpc();

            double closestDist = Double.MAX_VALUE;
            Player closest = null;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().equals(npc.getWorld())) {
                    if (npc.isShown(player)) npc.hide(player);
                    continue;
                }
                double distance = player.getLocation().distanceSquared(npc.getBukkitLocation());

                // visibility
                boolean inRange = npc instanceof ModeledNpcImpl ? distance <= NumberConversions.square(((ModeledNpcImpl) npc).getRangeManager().getRenderDistance()) : distance <= distSq;
                if (!inRange && npc.isShown(player)) {
                    NpcDespawnEvent event = new NpcDespawnEvent(player, entry);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) npc.hide(player);
                }
                if (inRange) {
                    if (!npc.isShown(player)) {
                        NpcSpawnEvent event = new NpcSpawnEvent(player, entry);
                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled()) continue;
                        npc.show(player);
                    }
                    if (distance < closestDist) {
                        closestDist = distance;
                        closest = player;
                    }
                }
            }
            // look property
            if (closest != null && npc.getProperty(lookProperty) && lookPropertyDistSq >= closestDist) {
                double offset = 0;
                if (npc.getType() != null)  offset = -npc.getType().getHologramOffset();
                NpcLocation expected = npc.getNpcLocation().lookingAt(closest.getLocation().add(0, offset, 0));
                if (!expected.equals(npc.getNpcLocation())) npc.setNpcLocation(expected);
            }
        }
    }
}
