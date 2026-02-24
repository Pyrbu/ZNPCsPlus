package lol.pyr.znpcsplus.interaction;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import java.util.Collections;
import lol.pyr.znpcsplus.api.event.NpcInteractEvent;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.user.User;
import lol.pyr.znpcsplus.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InteractionPacketListener implements PacketListener {
  private final UserManager userManager;
  private final NpcRegistryImpl npcRegistry;
  private final NpcTypeRegistryImpl typeRegistry;
  private final TaskScheduler scheduler;

  public InteractionPacketListener(
      UserManager userManager,
      NpcRegistryImpl npcRegistry,
      NpcTypeRegistryImpl typeRegistry,
      TaskScheduler scheduler) {
    this.userManager = userManager;
    this.npcRegistry = npcRegistry;
    this.typeRegistry = typeRegistry;
    this.scheduler = scheduler;
  }

  @Override
  public void onPacketReceive(PacketReceiveEvent event) {
    if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;
    Player player = (Player) event.getPlayer();
    if (player == null) return;

    WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);

    NpcEntryImpl entry = npcRegistry.getByEntityId(packet.getEntityId());
    if (entry == null || !entry.isProcessed()) return;
    NpcImpl npc = entry.getNpc();

    if ((packet.getAction().equals(WrapperPlayClientInteractEntity.InteractAction.INTERACT)
            || packet
                .getAction()
                .equals(WrapperPlayClientInteractEntity.InteractAction.INTERACT_AT))
        && npc.getType().equals(typeRegistry.getByName("allay"))) {
      PacketEvents.getAPI()
          .getPlayerManager()
          .sendPacket(
              player,
              new WrapperPlayServerEntityEquipment(
                  packet.getEntityId(),
                  Collections.singletonList(
                      new Equipment(EquipmentSlot.MAIN_HAND, ItemStack.EMPTY))));
      player.updateInventory();
    }

    InteractionType type = wrapClickType(packet.getAction());

    User user = userManager.get(player);
    if (!user.canInteract()) return;

    NpcInteractEvent interactEvent = new NpcInteractEvent(player, entry, type);
    Bukkit.getPluginManager().callEvent(interactEvent);
    if (interactEvent.isCancelled()) return;

    for (InteractionAction action : npc.getActions()) {
      if (action.getInteractionType() != InteractionType.ANY_CLICK
          && action.getInteractionType() != type) continue;
      if (action.getCooldown() > 0 && !user.actionCooldownCheck(action)) continue;
      scheduler.runLaterSync(() -> action.run(player), action.getDelay());
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
