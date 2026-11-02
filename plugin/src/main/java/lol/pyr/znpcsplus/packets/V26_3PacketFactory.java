package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemSwingAnimation;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSwingAnimation;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class V26_3PacketFactory extends V1_21_3PacketFactory {
    public V26_3PacketFactory(TaskScheduler scheduler, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer, ConfigManager configManager) {
        super(scheduler, packetEvents, propertyRegistry, textSerializer, configManager);
    }

    @Override
    public void sendHandSwing(Player player, PacketEntity entity, boolean offHand) {
        sendPacket(player, new WrapperPlayServerSwingAnimation(entity.getEntityId(),
                offHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND,
                new ItemSwingAnimation(ItemSwingAnimation.Type.WHACK, 6)));
    }
}
