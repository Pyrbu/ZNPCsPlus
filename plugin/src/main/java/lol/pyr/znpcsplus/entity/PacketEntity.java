package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.reflection.Reflections;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class PacketEntity {
    private final PacketFactory packetFactory;

    private final PropertyHolder properties;
    private final int entityId;
    private final UUID uuid;

    private final EntityType type;
    private NpcLocation location;

    public PacketEntity(PacketFactory packetFactory, PropertyHolder properties, EntityType type, NpcLocation location) {
        this.packetFactory = packetFactory;
        this.properties = properties;
        this.entityId = reserveEntityID();
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.location = location;
    }

    public int getEntityId() {
        return entityId;
    }

    public NpcLocation getLocation() {
        return location;
    }

    public UUID getUuid() {
        return uuid;
    }

    public EntityType getType() {
        return type;
    }

    public void setLocation(NpcLocation location, Collection<Player> viewers) {
        this.location = location;
        for (Player viewer : viewers) packetFactory.teleportEntity(viewer, this);
    }

    public void spawn(Player player) {
        if (type == EntityTypes.PLAYER) packetFactory.spawnPlayer(player, this, properties);
        else packetFactory.spawnEntity(player, this, properties);
    }

    public void despawn(Player player) {
        packetFactory.destroyEntity(player, this, properties);
    }

    public void refreshMeta(Player player) {
        packetFactory.sendAllMetadata(player, this, properties);
    }

    private static int reserveEntityID() {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14)) {
            return Reflections.ATOMIC_ENTITY_ID_FIELD.get().incrementAndGet();
        } else {
            int id = Reflections.ENTITY_ID_MODIFIER.get();
            Reflections.ENTITY_ID_MODIFIER.set(id + 1);
            return id;
        }
    }
}
