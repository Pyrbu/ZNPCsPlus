package io.github.znetworkw.znpcservers.npc.interaction;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCAction;
import io.github.znetworkw.znpcservers.user.ZUser;
import lol.pyr.znpcsplus.ZNPCsPlus;
import org.bukkit.Bukkit;

import java.util.List;

public class InteractionPacketListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
        ZUser user = ZUser.find(event.getUser().getUUID());

        long lastInteract = System.nanoTime() - user.getLastInteract();
        if (user.getLastInteract() != 0L && lastInteract < 1000000000L) return;

        NPC npc = NPC.all().stream().filter(n -> n.getEntityID() == packet.getEntityId()).findFirst().orElse(null);
        if (npc == null) return;

        ClickType clickType = ClickType.forName(packet.getAction().name());
        user.updateLastInteract();
        ZNPCsPlus.SCHEDULER.runTask(() -> {
            Bukkit.getServer().getPluginManager().callEvent(new NPCInteractEvent(user.toPlayer(), clickType, npc));
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
