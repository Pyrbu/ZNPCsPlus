package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.api.event.NpcDespawnEvent;
import lol.pyr.znpcsplus.api.event.NpcSpawnEvent;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.LookType;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

public class NpcProcessorTask extends BukkitRunnable {
    private final NpcRegistryImpl npcRegistry;
    private final EntityPropertyRegistryImpl propertyRegistry;

    public NpcProcessorTask(NpcRegistryImpl npcRegistry, EntityPropertyRegistryImpl propertyRegistry) {
        this.npcRegistry = npcRegistry;
        this.propertyRegistry = propertyRegistry;
    }

    public void run() {
        EntityPropertyImpl<Integer> viewDistanceProperty = propertyRegistry.getByName("view_distance", Integer.class); // Not sure why this is an Integer, but it is
        EntityPropertyImpl<LookType> lookProperty = propertyRegistry.getByName("look", LookType.class);
        EntityPropertyImpl<Double> lookDistanceProperty = propertyRegistry.getByName("look_distance", Double.class);
        EntityPropertyImpl<Boolean> permissionRequiredProperty = propertyRegistry.getByName("permission_required", Boolean.class);
        double lookDistance;
        for (NpcEntryImpl entry : npcRegistry.getProcessable()) {
            NpcImpl npc = entry.getNpc();
            if (!npc.isEnabled()) continue;

            double closestDist = Double.MAX_VALUE;
            Player closest = null;
            LookType lookType = npc.getProperty(lookProperty);
            lookDistance =  NumberConversions.square(npc.getProperty(lookDistanceProperty));
            boolean permissionRequired = npc.getProperty(permissionRequiredProperty);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().equals(npc.getWorld())) {
                    if (npc.isVisibleTo(player)) npc.hide(player);
                    continue;
                }
                if (permissionRequired && !player.hasPermission("znpcsplus.npc." + entry.getId())) {
                    if (npc.isVisibleTo(player)) npc.hide(player);
                    continue;
                }
                double distance = player.getLocation().distanceSquared(npc.getBukkitLocation());

                // visibility
                boolean inRange = distance <= NumberConversions.square(npc.getProperty(viewDistanceProperty));
                if (!inRange && npc.isVisibleTo(player)) {
                    NpcDespawnEvent event = new NpcDespawnEvent(player, entry);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) npc.hide(player);
                }
                if (inRange) {
                    if (!npc.isVisibleTo(player)) {
                        NpcSpawnEvent event = new NpcSpawnEvent(player, entry);
                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled()) continue;
                        npc.show(player);
                    }
                    if (distance < closestDist) {
                        closestDist = distance;
                        closest = player;
                    }
                    if (lookType.equals(LookType.PER_PLAYER) && lookDistance >= distance) {
                        NpcLocation expected = npc.getLocation().lookingAt(player.getLocation().add(0, -npc.getType().getHologramOffset(), 0));
                        if (!expected.equals(npc.getLocation())) npc.setHeadRotation(player, expected.getYaw(), expected.getPitch());
                    }
                }
            }
            // look property
            if (lookType.equals(LookType.CLOSEST_PLAYER)) {
                if (closest != null && lookDistance >= closestDist) {
                    NpcLocation expected = npc.getLocation().lookingAt(closest.getLocation().add(0, -npc.getType().getHologramOffset(), 0));
                    if (!expected.equals(npc.getLocation())) npc.setHeadRotation(expected.getYaw(), expected.getPitch());
                }
            }
        }
    }
}
