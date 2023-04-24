package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.utility.Utils;
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
        if (type == EntityTypes.PLAYER) throw new RuntimeException("Wrong class used for player");
        this.entityId = reserveEntityID();
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
        PacketFactory.get().spawnEntity(player, this);
    }

    public void despawn(Player player) {
        PacketFactory.get().destroyEntity(player, this);
    }

    private static int reserveEntityID() {
        if (Utils.versionNewer(14)) return Reflections.ATOMIC_ENTITY_ID_FIELD.get().incrementAndGet();
        else {
            int id = Reflections.ENTITY_ID_MODIFIER.get();
            Reflections.ENTITY_ID_MODIFIER.set(id + 1);
            return id;
        }
    }
}
