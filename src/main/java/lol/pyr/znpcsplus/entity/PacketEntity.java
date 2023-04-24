package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.packets.PacketFactory;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class PacketEntity {
    private final int entityId;
    private final UUID uuid;

    private final EntityType type;
    private PacketLocation location;

    public PacketEntity(EntityType type, PacketLocation location) {
        this.entityId = EntityIDProvider.reserve();
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.location = location;
    }

    public int getEntityId() {
        return entityId;
    }

    public PacketLocation getLocation() {
        return location;
    }

    public UUID getUuid() {
        return uuid;
    }

    public EntityType getType() {
        return type;
    }

    public void setLocation(PacketLocation location, Set<Player> viewers) {
        this.location = location;
        for (Player viewer : viewers) PacketFactory.get().teleportEntity(viewer, this);
    }

    public void spawn(Player player) {
        if (type == EntityTypes.PLAYER) PacketFactory.get().spawnPlayer(player, this);
        else PacketFactory.get().spawnEntity(player, this);
    }

    public void despawn(Player player) {
        PacketFactory.get().destroyEntity(player, this);
    }
}
