package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.reflection.Reflections;
import lol.pyr.znpcsplus.util.FutureUtil;
import lol.pyr.znpcsplus.util.NpcLocation;
import lol.pyr.znpcsplus.util.Viewable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PacketEntity implements PropertyHolder {
    private final PacketFactory packetFactory;

    private final PropertyHolder properties;
    private final Viewable viewable;
    private final int entityId;
    private final UUID uuid;

    private final EntityType type;
    private NpcLocation location;

    private PacketEntity vehicle;
    private Integer vehicleId;
    private List<Integer> passengers;
    private boolean listedInTabList = true;

    public PacketEntity(PacketFactory packetFactory, PropertyHolder properties, Viewable viewable, EntityType type, NpcLocation location) {
        this.packetFactory = packetFactory;
        this.properties = properties;
        this.viewable = viewable;
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

    public void setLocation(NpcLocation location) {
        this.location = location;
        if (vehicle != null) {
            vehicle.setLocation(location.withY(location.getY() - 0.9));
            return;
        }
        for (Player viewer : viewable.getViewers()) packetFactory.teleportEntity(viewer, this);
    }

    public CompletableFuture<Void> spawn(Player player) {
        return FutureUtil.exceptionPrintingRunAsync(() -> {
            if (type == EntityTypes.PLAYER) packetFactory.spawnPlayer(player, this, properties).join();
            else packetFactory.spawnEntity(player, this, properties);
            if (vehicle != null) {
                setVehicle(vehicle);
            }
            if (vehicleId != null) {
                packetFactory.setPassengers(player, vehicleId, this.getEntityId());
            }
            if (passengers != null) {
                packetFactory.setPassengers(player, this.getEntityId(), passengers.stream().mapToInt(Integer::intValue).toArray());
            }
        });
    }

    public void setHeadRotation(Player player, float yaw, float pitch) {
        packetFactory.sendHeadRotation(player, this, yaw, pitch);
    }

    public PacketEntity getVehicle() {
        return vehicle;
    }

    public Viewable getViewable() {
        return viewable;
    }

    public void setVehicleId(Integer vehicleId) {
        if (this.vehicle != null) {
            for (Player player : viewable.getViewers()) {
                packetFactory.setPassengers(player, this.vehicle.getEntityId());
                this.vehicle.despawn(player);
                packetFactory.teleportEntity(player, this);
            }
        } else if (this.vehicleId != null) {
            for (Player player : viewable.getViewers()) {
                packetFactory.setPassengers(player, this.vehicleId);
            }
        }
        this.vehicleId = vehicleId;
        if (vehicleId == null) return;

        for (Player player : viewable.getViewers()) {
            packetFactory.setPassengers(player, this.getEntityId(), vehicleId);
        }
    }

    public void setVehicle(PacketEntity vehicle) {
        // remove old vehicle
        if (this.vehicle != null) {
            for (Player player : viewable.getViewers()) {
                packetFactory.setPassengers(player, this.vehicle.getEntityId());
                this.vehicle.despawn(player);
                packetFactory.teleportEntity(player, this);
            }
        } else if (this.vehicleId != null) {
            for (Player player : viewable.getViewers()) {
                packetFactory.setPassengers(player, this.vehicleId);
            }
        }

        this.vehicle = vehicle;
        if (this.vehicle == null) return;

        vehicle.setLocation(location.withY(location.getY() - 0.9));
        for (Player player : viewable.getViewers()) {
            vehicle.spawn(player).thenRun(() -> {
                packetFactory.setPassengers(player, vehicle.getEntityId(), this.getEntityId());
            });
        }
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public List<Integer> getPassengers() {
        return passengers == null ? Collections.emptyList() : passengers;
    }

    public void addPassenger(int entityId) {
        if (passengers == null) {
            passengers = new ArrayList<>();
        }
        passengers.add(entityId);
        for (Player player : viewable.getViewers()) {
            packetFactory.setPassengers(player, this.getEntityId(), passengers.stream().mapToInt(Integer::intValue).toArray());
        }
    }

    public void removePassenger(int entityId) {
        if (passengers == null) return;
        passengers.remove(entityId);
        for (Player player : viewable.getViewers()) {
            packetFactory.setPassengers(player, this.getEntityId(), passengers.stream().mapToInt(Integer::intValue).toArray());
        }
        if (passengers.isEmpty()) {
            passengers = null;
        }
    }

    public void despawn(Player player) {
        packetFactory.destroyEntity(player, this, properties);
        if (vehicle != null) vehicle.despawn(player);
    }

    public void refreshMeta(Player player) {
        packetFactory.sendAllMetadata(player, this, properties);
    }

    public void swingHand(Player player, boolean offhand) {
        packetFactory.sendHandSwing(player, this, offhand);
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

    @Override
    public <T> T getProperty(EntityProperty<T> key) {
        return properties.getProperty(key);
    }

    @Override
    public boolean hasProperty(EntityProperty<?> key) {
        return properties.hasProperty(key);
    }

    @Override
    public <T> void setProperty(EntityProperty<T> key, T value) {
        properties.setProperty(key, value);
    }

    @Override
    public void setItemProperty(EntityProperty<?> key, ItemStack value) {
        properties.setItemProperty(key, value);
    }

    @Override
    public ItemStack getItemProperty(EntityProperty<?> key) {
        return properties.getItemProperty(key);
    }

    public PropertyHolder getProperties() {
        return properties;
    }

    @Override
    public Set<EntityProperty<?>> getAppliedProperties() {
        return properties.getAppliedProperties();
    }

    public boolean isListedInTabList() {
        return listedInTabList;
    }

    public void setListedInTabList(boolean listedInTabList) {
        this.listedInTabList = listedInTabList;
    }
}
