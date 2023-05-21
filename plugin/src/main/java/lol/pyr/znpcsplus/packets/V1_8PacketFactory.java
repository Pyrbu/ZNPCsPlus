package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.util.ZLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class V1_8PacketFactory implements PacketFactory {
    protected final TaskScheduler scheduler;
    protected final MetadataFactory metadataFactory;
    protected final PacketEventsAPI<Plugin> packetEvents;

    public V1_8PacketFactory(TaskScheduler scheduler, MetadataFactory metadataFactory, PacketEventsAPI<Plugin> packetEvents) {
        this.scheduler = scheduler;
        this.metadataFactory = metadataFactory;
        this.packetEvents = packetEvents;
    }

    @Override
    public void spawnPlayer(Player player, PacketEntity entity, PropertyHolder properties) {
        addTabPlayer(player, entity, properties).thenAccept(ignored -> {
            createTeam(player, entity, properties);
            ZLocation location = entity.getLocation();
            sendPacket(player, new WrapperPlayServerSpawnPlayer(entity.getEntityId(),
                    entity.getUuid(), location.toVector3d(), location.getYaw(), location.getPitch(), Collections.emptyList()));
            sendPacket(player, new WrapperPlayServerEntityHeadLook(entity.getEntityId(), location.getYaw()));
            sendAllMetadata(player, entity, properties);
            scheduler.runLaterAsync(() -> removeTabPlayer(player, entity), 60);
        });
    }

    @Override
    public void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        ZLocation location = entity.getLocation();
        EntityType type = entity.getType();
        ClientVersion clientVersion = packetEvents.getServerManager().getVersion().toClientVersion();
        sendPacket(player, type.getLegacyId(clientVersion) == -1 ?
                new WrapperPlayServerSpawnLivingEntity(entity.getEntityId(), entity.getUuid(), type, location.toVector3d(),
                        location.getYaw(), location.getPitch(), location.getPitch(), new Vector3d(), Collections.emptyList()) :
                new WrapperPlayServerSpawnEntity(entity.getEntityId(), Optional.of(entity.getUuid()), entity.getType(), location.toVector3d(),
                        location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.empty()));
        sendAllMetadata(player, entity, properties);
        createTeam(player, entity, properties);
    }

    @Override
    public void destroyEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        sendPacket(player, new WrapperPlayServerDestroyEntities(entity.getEntityId()));
        removeTeam(player, entity);
    }

    @Override
    public void teleportEntity(Player player, PacketEntity entity) {
        ZLocation location = entity.getLocation();
        sendPacket(player, new WrapperPlayServerEntityTeleport(entity.getEntityId(), location.toVector3d(), location.getYaw(), location.getPitch(), true));
        if (entity.getType() == EntityTypes.PLAYER) sendPacket(player, new WrapperPlayServerEntityHeadLook(entity.getEntityId(), location.getYaw()));
    }

    @Override
    public CompletableFuture<Void> addTabPlayer(Player player, PacketEntity entity, PropertyHolder properties) {
        if (entity.getType() != EntityTypes.PLAYER) return CompletableFuture.completedFuture(null);
        CompletableFuture<Void> future = new CompletableFuture<>();
        skinned(player, properties, new UserProfile(entity.getUuid(), Integer.toString(entity.getEntityId()))).thenAccept(profile -> {
            sendPacket(player, new WrapperPlayServerPlayerInfo(
                    WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData(Component.text(""),
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
    public void createTeam(Player player, PacketEntity entity, PropertyHolder properties) {
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.CREATE, new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                Component.empty(), Component.empty(), Component.empty(),
                WrapperPlayServerTeams.NameTagVisibility.NEVER,
                WrapperPlayServerTeams.CollisionRule.NEVER,
                properties.hasProperty(EntityPropertyImpl.GLOW) ? properties.getProperty(EntityPropertyImpl.GLOW) : NamedTextColor.WHITE,
                WrapperPlayServerTeams.OptionData.NONE
        )));
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.ADD_ENTITIES, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null,
                entity.getType() == EntityTypes.PLAYER ? Integer.toString(entity.getEntityId()) : entity.getUuid().toString()));
    }

    @Override
    public void removeTeam(Player player, PacketEntity entity) {
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.REMOVE, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null));
    }

    @Override
    public Map<Integer, EntityData> generateMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        HashMap<Integer, EntityData> data = new HashMap<>();
        if (entity.getType() == EntityTypes.PLAYER) add(data, metadataFactory.skinLayers(properties.getProperty(EntityPropertyImpl.SKIN_LAYERS)));
        add(data, metadataFactory.effects(properties.getProperty(EntityPropertyImpl.FIRE), false, properties.getProperty(EntityPropertyImpl.INVISIBLE)));
        add(data, metadataFactory.silent(properties.getProperty(EntityPropertyImpl.SILENT)));
        if (properties.hasProperty(EntityPropertyImpl.NAME)) addAll(data, metadataFactory.name(properties.getProperty(EntityPropertyImpl.NAME)));
        return data;
    }

    @Override
    public void sendAllMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        sendMetadata(player, entity, new ArrayList<>(generateMetadata(player, entity, properties).values()));
    }

    @Override
    public void sendMetadata(Player player, PacketEntity entity, List<EntityData> data) {
        packetEvents.getPlayerManager().sendPacket(player, new WrapperPlayServerEntityMetadata(entity.getEntityId(), data));
    }

    protected void sendPacket(Player player, PacketWrapper<?> packet) {
        packetEvents.getPlayerManager().sendPacket(player, packet);
    }

    protected CompletableFuture<UserProfile> skinned(Player player, PropertyHolder properties, UserProfile profile) {
        if (!properties.hasProperty(EntityPropertyImpl.SKIN)) return CompletableFuture.completedFuture(profile);
        BaseSkinDescriptor descriptor = (BaseSkinDescriptor) properties.getProperty(EntityPropertyImpl.SKIN);
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

    protected void add(Map<Integer, EntityData> map, EntityData data) {
        map.put(data.getIndex(), data);
    }

    protected void addAll(Map<Integer, EntityData> map, Collection<EntityData> data) {
        for (EntityData d : data) add(map, d);
    }
}
