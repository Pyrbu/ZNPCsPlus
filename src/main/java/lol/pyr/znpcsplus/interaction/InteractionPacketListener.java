package lol.pyr.znpcsplus.interaction;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import lol.pyr.znpcsplus.ZNPCsPlus;
import lol.pyr.znpcsplus.npc.NPC;
import lol.pyr.znpcsplus.npc.NPCRegistry;
import lol.pyr.znpcsplus.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class InteractionPacketListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;
        Player player = (Player) event.getPlayer();

        WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
        User user = User.get(player);
        if (!user.canInteract()) return;

        NPC npc = NPCRegistry.getByEntityId(packet.getEntityId());
        if (npc == null) return;

        ZNPCsPlus.SCHEDULER.runNextTick(() -> {
            Bukkit.getServer().getPluginManager().callEvent(new NPCInteractEvent(player, clickType, npc));
            List<NPCAction> actions = npc.getNpcPojo().getClickActions();
            if (actions == null) return;
            for (NPCAction action : actions) {
                if (action.getClickType() != ClickType.DEFAULT && action.getClickType() != clickType) continue;
                if (action.getDelay() > 0) {
                    int actionId = npc.getNpcPojo().getClickActions().indexOf(action);
                    if (user.getLastClicked().containsKey(actionId) && System.nanoTime() - user.getLastClicked().get(actionId) < action.getFixedDelay()) continue;
                    user.getLastClicked().put(actionId, System.nanoTime());
                }
                action.run(user, action.getAction());
            }
        });
    }
}
