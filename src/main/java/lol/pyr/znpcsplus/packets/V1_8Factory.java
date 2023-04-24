package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.entity.PacketLocation;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class V1_8Factory implements PacketFactory {
    @Override
    public void spawnPlayer(Player player, PacketEntity entity) {
        addTabPlayer(player, entity);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerSpawnPlayer());
        // TODO
    }

    @Override
    public void spawnEntity(Player player, PacketEntity entity) {
        PacketLocation location = entity.getLocation();
        EntityType type = entity.getType();
        ClientVersion clientVersion = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, type.getLegacyId(clientVersion) == -1 ?
                new WrapperPlayServerSpawnLivingEntity(entity.getEntityId(), new UUID(0, 0), type, location.toVector3d(),
                        location.getYaw(), location.getPitch(), location.getPitch(), new Vector3d(), List.of()) :
                new WrapperPlayServerSpawnEntity(entity.getEntityId(), Optional.empty(), entity.getType(), location.toVector3d(),
                        location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.empty()));
    }

    @Override
    public void destroyEntity(Player player, PacketEntity entity) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerDestroyEntities(entity.getEntityId()));
    }

    @Override
    public void teleportEntity(Player player, PacketEntity entity) {
        PacketLocation location = entity.getLocation();
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerEntityTeleport(entity.getEntityId(),
                location.toVector3d(), location.getYaw(), location.getPitch(), true));
    }

    @Override
    public void addTabPlayer(Player player, PacketEntity entity) {

    }

    @Override
    public void removeTabPlayer(Player player, PacketEntity entity) {
        new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData(null, new UserProfile(gameProfile.getId(), gameProfile.getName()), null, 1))
    }
}
