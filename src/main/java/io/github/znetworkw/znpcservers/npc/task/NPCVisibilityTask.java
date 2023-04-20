package io.github.znetworkw.znpcservers.npc.task;

import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.user.ZUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCVisibilityTask extends BukkitRunnable {
    public NPCVisibilityTask(Plugin serversNPC) {
        runTaskTimerAsynchronously(serversNPC, 60L, 10L);
    }

    public void run() {
        int distSq = ConfigurationConstants.VIEW_DISTANCE * ConfigurationConstants.VIEW_DISTANCE;
        for (NPC npc : NPC.all()) for (Player player : Bukkit.getOnlinePlayers()) {
            ZUser zUser = ZUser.find(player);
            boolean canSeeNPC = (player.getWorld() == npc.getLocation().getWorld() && player.getLocation().distanceSquared(npc.getLocation()) <= distSq);
            if (npc.getViewers().contains(zUser) && !canSeeNPC) {
                npc.delete(zUser);
                continue;
            }
            if (canSeeNPC) {
                if (!npc.getViewers().contains(zUser)) npc.spawn(zUser);
                npc.getHologram().updateNames(zUser);
                ConversationModel conversationStorage = npc.getNpcPojo().getConversation();
                if (conversationStorage != null && conversationStorage.getConversationType() == ConversationModel.ConversationType.RADIUS) npc.tryStartConversation(player);
            }
        }
    }
}
