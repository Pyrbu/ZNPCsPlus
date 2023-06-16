package lol.pyr.znpcsplus.interaction;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.user.User;
import lol.pyr.znpcsplus.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InteractionPacketListener implements PacketListener {
    private final UserManager userManager;
    private final NpcRegistryImpl npcRegistry;

    public InteractionPacketListener(UserManager userManager, NpcRegistryImpl npcRegistry) {
        this.userManager = userManager;
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;
        Player player = (Player) event.getPlayer();

        WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
        User user = userManager.get(player);
        if (!user.canInteract()) return;

        NpcEntryImpl entry = npcRegistry.getByEntityId(packet.getEntityId());
        if (entry == null || !entry.isProcessed()) {
            return;
        }
        Npc npc = entry.getNpc();
        InteractionType type = wrapClickType(packet.getAction());

        NpcInteractEvent interactEvent = new NpcInteractEvent(player, entry, type);
        Bukkit.getPluginManager().callEvent(interactEvent);
        if (interactEvent.isCancelled()) return;

        for (InteractionAction action : npc.getActions()) {
            if (action.getInteractionType() != InteractionType.ANY_CLICK && action.getInteractionType() != type) continue;
            if (action.getCooldown() > 0 && !user.actionCooldownCheck(action)) continue;
            action.run(player);
        }
    }

    private InteractionType wrapClickType(WrapperPlayClientInteractEntity.InteractAction action) {
        switch (action) {
            case ATTACK:
                return InteractionType.LEFT_CLICK;
            case INTERACT:
            case INTERACT_AT:
                return InteractionType.RIGHT_CLICK;
        }
        throw new IllegalStateException();
    }
}
