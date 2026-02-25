package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.PapiUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public class V1_19_3PacketFactory extends V1_17PacketFactory {
    public V1_19_3PacketFactory(TaskScheduler scheduler, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer, ConfigManager configManager) {
        super(scheduler, packetEvents, propertyRegistry, textSerializer, configManager);
    }

    @Override
    public CompletableFuture<Void> addTabPlayer(Player player, PacketEntity entity, PropertyHolder properties) {
        if (entity.getType() != EntityTypes.PLAYER) return CompletableFuture.completedFuture(null);
        CompletableFuture<Void> future = new CompletableFuture<>();
        Component tabListDisplayName = tabListDisplayNameProperty != null && properties.hasProperty(tabListDisplayNameProperty.get()) ?
                PapiUtil.set(textSerializer, player, properties.getProperty(tabListDisplayNameProperty.get())) :
                Component.text(PapiUtil.set(player, configManager.getConfig().tabDisplayName()
                        .replace("{id}", Integer.toString(entity.getEntityId()))
                        .replace("{name}", displayNameProperty != null && properties.hasProperty(displayNameProperty.get()) ?
                                properties.getProperty(displayNameProperty.get()) :
                                "")
                ));
        boolean listed = alwaysVisibleInTabProperty == null || properties.getProperty(alwaysVisibleInTabProperty.get());

        // This is to set the entity name for NPCs
        String displayName = displayNameProperty != null && properties.hasProperty(displayNameProperty.get()) ?
                properties.getProperty(displayNameProperty.get()) : Integer.toString(entity.getEntityId());
        skinned(player, properties, new UserProfile(entity.getUuid(), displayName)).thenAccept(profile -> {
            WrapperPlayServerPlayerInfoUpdate.PlayerInfo info = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                    profile, listed, 1, GameMode.CREATIVE,
                    tabListDisplayName, null);
            sendPacket(player, new WrapperPlayServerPlayerInfoUpdate(EnumSet.of(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
                    WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED, WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME),
                    info, info, info));
            entity.setListedInTabList(true);
            future.complete(null);
        });
        return future;
    }

    @Override
    public void removeTabPlayer(Player player, PacketEntity entity) {
        if (entity.getType() != EntityTypes.PLAYER) return;
        if (!entity.isListedInTabList()) return;
        sendPacket(player, new WrapperPlayServerPlayerInfoRemove(entity.getUuid()));
        entity.setListedInTabList(false);
    }

    @Override
    public void updateListed(Player player, PacketEntity entity, boolean listed) {
        if (entity.getType() != EntityTypes.PLAYER) return;
        sendPacket(player, new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED,
                new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(new UserProfile(entity.getUuid(), null),
                        listed, 1, GameMode.CREATIVE, null, null))
        );
        entity.setListedInTabList(listed);
    }

    @Override
    public void updateDisplayName(Player player, PacketEntity entity, Component displayName) {
        if (entity.getType() != EntityTypes.PLAYER) return;
        sendPacket(player, new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME,
                new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(new UserProfile(entity.getUuid(), null),
                        entity.isListedInTabList(), 1, GameMode.CREATIVE, displayName, null))
        );
    }
}