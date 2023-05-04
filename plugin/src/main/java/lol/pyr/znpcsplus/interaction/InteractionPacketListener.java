package lol.pyr.znpcsplus.interaction;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.user.User;
import org.bukkit.entity.Player;

public class InteractionPacketListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;
        Player player = (Player) event.getPlayer();

        WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
        User user = User.get(player);
        if (!user.canInteract()) return;

        NpcEntryImpl entry = NpcRegistryImpl.get().getByEntityId(packet.getEntityId());
        if (entry == null || !entry.isProcessed()) return;
        NpcImpl npc = entry.getNpc();

        for (NpcAction action : npc.getActions()) {
            if (action.getCooldown() > 0 && !user.actionCooldownCheck(action)) continue;
            action.run(player);
        }
    }
}
