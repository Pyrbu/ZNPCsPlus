package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramRefreshTask extends BukkitRunnable {
    private final NpcRegistryImpl npcRegistry;

    public HologramRefreshTask(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run() {
        for (NpcEntryImpl entry : npcRegistry.getProcessable()) {
            HologramImpl hologram = entry.getNpc().getHologram();
            if (hologram.shouldRefresh()) hologram.refresh();
        }
    }
}
