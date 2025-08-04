package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.entity.properties.attributes.AttributeProperty;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.util.NamedColor;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class V1_8PacketFactory implements PacketFactory {
    protected final TaskScheduler scheduler;
    protected final PacketEventsAPI<Plugin> packetEvents;
    protected final EntityPropertyRegistryImpl propertyRegistry;
    protected final LegacyComponentSerializer textSerializer;
    protected ConfigManager configManager;

    public V1_8PacketFactory(TaskScheduler scheduler, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer, ConfigManager configManager) {
        this.scheduler = scheduler;
        this.packetEvents = packetEvents;
        this.propertyRegistry = propertyRegistry;
        this.textSerializer = textSerializer;
        this.configManager = configManager;
    }

    @Override
    public CompletableFuture<Void> spawnPlayer(Player player, PacketEntity entity, PropertyHolder properties) {
        return addTabPlayer(player, entity, properties).thenAccept(ignored -> {
            createTeam(player, entity, properties.getProperty(propertyRegistry.getByName("glow", NamedColor.class)));
            NpcLocation location = entity.getLocation();
            sendPacket(player, new WrapperPlayServerSpawnPlayer(entity.getEntityId(),
                    entity.getUuid(), npcLocationToVector(location), location.getYaw(), location.getPitch(), Collections.emptyList()));
            sendPacket(player, new WrapperPlayServerEntityHeadLook(entity.getEntityId(), location.getYaw()));
            sendAllMetadata(player, entity, properties);
            sendAllAttributes(player, entity, properties);
            scheduler.runLaterAsync(() -> removeTabPlayer(player, entity), configManager.getConfig().tabHideDelay());
        });
    }

    @Override
    public void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        NpcLocation location = entity.getLocation();
        EntityType type = entity.getType();
        ClientVersion clientVersion = packetEvents.getServerManager().getVersion().toClientVersion();
        sendPacket(player, type.getLegacyId(clientVersion) == -1 ?
                new WrapperPlayServerSpawnLivingEntity(entity.getEntityId(), entity.getUuid(), type, npcLocationToVector(location),
                        location.getYaw(), location.getPitch(), location.getYaw(), new Vector3d(), Collections.emptyList()) :
                new WrapperPlayServerSpawnEntity(entity.getEntityId(), Optional.of(entity.getUuid()), entity.getType(), npcLocationToVector(location),
                        location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.empty()));
        sendAllMetadata(player, entity, properties);
        if (EntityTypes.isTypeInstanceOf(type, EntityTypes.LIVINGENTITY)) sendAllAttributes(player, entity, properties);
        createTeam(player, entity, properties.getProperty(propertyRegistry.getByName("glow", NamedColor.class)));
    }

    protected Vector3d npcLocationToVector(NpcLocation location) {
        return new Vector3d(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void destroyEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        sendPacket(player, new WrapperPlayServerDestroyEntities(entity.getEntityId()));
        removeTeam(player, entity);
    }

    @Override
    public void teleportEntity(Player player, PacketEntity entity) {
        NpcLocation location = entity.getLocation();
        sendPacket(player, new WrapperPlayServerEntityTeleport(entity.getEntityId(), npcLocationToVector(location), location.getYaw(), location.getPitch(), true));
        sendPacket(player, new WrapperPlayServerEntityHeadLook(entity.getEntityId(), location.getYaw()));
    }

    @Override
    public CompletableFuture<Void> addTabPlayer(Player player, PacketEntity entity, PropertyHolder properties) {
        if (entity.getType() != EntityTypes.PLAYER) return CompletableFuture.completedFuture(null);
        CompletableFuture<Void> future = new CompletableFuture<>();
        skinned(player, properties, new UserProfile(entity.getUuid(), Integer.toString(entity.getEntityId()))).thenAccept(profile -> {
            sendPacket(player, new WrapperPlayServerPlayerInfo(
                    WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData(
                            Component.text(configManager.getConfig().tabDisplayName().replace("{id}", Integer.toString(entity.getEntityId()))),
                    profile, GameMode.CREATIVE, 1)));
            future.complete(null);
        });
        return future;
    }

    @Override
    public void removeTabPlayer(Player player, PacketEntity entity) {
        if (entity.getType() != EntityTypes.PLAYER) return;
        sendPacket(player, new WrapperPlayServerPlayerInfo(
                WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData(null,
                new UserProfile(entity.getUuid(), null), null, -1)));
    }

    @Override
    public void createTeam(Player player, PacketEntity entity, NamedColor namedColor) {
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.CREATE, new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                Component.text(" "), null, null,
                WrapperPlayServerTeams.NameTagVisibility.NEVER,
                WrapperPlayServerTeams.CollisionRule.NEVER,
                namedColor == null ? NamedTextColor.WHITE : NamedTextColor.NAMES.value(namedColor.name().toLowerCase()),
                WrapperPlayServerTeams.OptionData.NONE
        )));

        String displayName = entity.getProperty(propertyRegistry.getByName("display_name", String.class)) == null ? entity.getEntityId()+"" : entity.getProperty(propertyRegistry.getByName("display_name", String.class));
        // 这个displayName替换掉原来的变量，是为了
        // '当NPC实体为玩家类型时，隐藏掉NPC头顶上的名称显示 不然会和hologram一起显示 导致有些混淆与冲突'
        // This displayName replaces the original variable in order to
        // 'hide the name display above the NPC's head when the NPC entity is a player type, otherwise it will be displayed together with the hologram, causing some confusion and conflict'
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.ADD_ENTITIES, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null,
                entity.getType() == EntityTypes.PLAYER ? displayName : entity.getUuid().toString()));
    }

    @Override
    public void removeTeam(Player player, PacketEntity entity) {
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.REMOVE, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null));
    }

    @Override
    public void sendAllMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        Map<Integer, EntityData<?>> datas = new HashMap<>();
        for (EntityProperty<?> property : properties.getAppliedProperties()) ((EntityPropertyImpl<?>) property).apply(player, entity, false, datas);
        sendMetadata(player, entity, new ArrayList<>(datas.values()));
    }

    @Override
    public void sendMetadata(Player player, PacketEntity entity, List<EntityData<?>> data) {
        sendPacket(player, new WrapperPlayServerEntityMetadata(entity.getEntityId(), data));
    }

    @Override
    public void sendHeadRotation(Player player, PacketEntity entity, float yaw, float pitch) {
        sendPacket(player, new WrapperPlayServerEntityHeadLook(entity.getEntityId(),yaw));
        sendPacket(player, new WrapperPlayServerEntityRotation(entity.getEntityId(), yaw, pitch, true));
    }

    @Override
    public void sendEquipment(Player player, PacketEntity entity, Equipment equipment) {
        sendPacket(player, new WrapperPlayServerEntityEquipment(entity.getEntityId(), Collections.singletonList(equipment)));
    }

    @Override
    public void setPassengers(Player player, int vehicleEntityId, int... passengers) {
        sendPacket(player, new WrapperPlayServerSetPassengers(vehicleEntityId, passengers));
    }

    protected void sendPacket(Player player, PacketWrapper<?> packet) {
        packetEvents.getPlayerManager().sendPacket(player, packet);
    }

    protected CompletableFuture<UserProfile> skinned(Player player, PropertyHolder properties, UserProfile profile) {
        if (!properties.hasProperty(propertyRegistry.getByName("skin"))) return CompletableFuture.completedFuture(profile);
        BaseSkinDescriptor descriptor = (BaseSkinDescriptor) properties.getProperty(propertyRegistry.getByName("skin", SkinDescriptor.class));
        if (descriptor.supportsInstant(player)) {
            descriptor.fetchInstant(player).apply(profile);
            return CompletableFuture.completedFuture(profile);
        }
        CompletableFuture<UserProfile> future = new CompletableFuture<>();
        descriptor.fetch(player).thenAccept(skin -> {
            if (skin != null) skin.apply(profile);
            future.complete(profile);
        });
        return future;
    }

    protected void add(Map<Integer, EntityData<?>> map, EntityData<?> data) {
        map.put(data.getIndex(), data);
    }

    @Override
    public void sendHandSwing(Player player, PacketEntity entity, boolean offHand) {
        sendPacket(player, new WrapperPlayServerEntityAnimation(entity.getEntityId(), offHand ?
                WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_OFF_HAND :
                WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM));
    }

    @Override
    public void sendAllAttributes(Player player, PacketEntity entity, PropertyHolder properties) {
        List<WrapperPlayServerUpdateAttributes.Property> attributesList = new ArrayList<>();
        properties.getAppliedProperties()
                .stream()
                .filter(property -> property instanceof AttributeProperty)
                .forEach(property -> ((AttributeProperty) property).apply(player, entity, false, attributesList));
        sendPacket(player, new WrapperPlayServerUpdateAttributes(entity.getEntityId(), attributesList));
    }

    @Override
    public void sendAttribute(Player player, PacketEntity entity, WrapperPlayServerUpdateAttributes.Property property) {
        sendPacket(player, new WrapperPlayServerUpdateAttributes(entity.getEntityId(), Collections.singletonList(property)));
    }
}
