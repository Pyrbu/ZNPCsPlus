package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NpcLocation;
import lol.pyr.znpcsplus.util.Viewable;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NpcImpl extends Viewable implements Npc {
    private final PacketFactory packetFactory;
    private String worldName;
    private PacketEntity entity;
    private NpcLocation location;
    private NpcTypeImpl type;
    private boolean enabled = true;
    private final HologramImpl hologram;
    private final UUID uuid;

    private final Map<EntityPropertyImpl<?>, Object> propertyMap = new HashMap<>();
    private final List<InteractionAction> actions = new ArrayList<>();

    private final Map<UUID, float[]> playerLookMap = new ConcurrentHashMap<>();

    protected NpcImpl(UUID uuid, EntityPropertyRegistryImpl propertyRegistry, ConfigManager configManager, LegacyComponentSerializer textSerializer, World world, NpcTypeImpl type, NpcLocation location, PacketFactory packetFactory) {
        this(uuid, propertyRegistry, configManager, packetFactory, textSerializer, world.getName(), type, location);
    }

    public NpcImpl(UUID uuid, EntityPropertyRegistryImpl propertyRegistry, ConfigManager configManager, PacketFactory packetFactory, LegacyComponentSerializer textSerializer, String world, NpcTypeImpl type, NpcLocation location) {
        this.packetFactory = packetFactory;
        this.worldName = world;
        this.type = type;
        this.location = location;
        this.uuid = uuid;
        entity = new PacketEntity(packetFactory, this, this, type.getType(), location);
        hologram = new HologramImpl(propertyRegistry, configManager, packetFactory, textSerializer, location.withY(location.getY() + type.getHologramOffset()));
    }

    public void setType(NpcTypeImpl type) {
        UNSAFE_hideAll();
        this.type = type;
        entity = new PacketEntity(packetFactory, this, this, type.getType(), entity.getLocation());
        hologram.setLocation(location.withY(location.getY() + type.getHologramOffset()));
        UNSAFE_showAll();
    }

    public void setType(NpcType type) {
        if (type == null) throw new IllegalArgumentException("Npc Type cannot be null");
        setType((NpcTypeImpl) type);
    }

    public NpcTypeImpl getType() {
        return type;
    }

    public PacketEntity getEntity() {
        return entity;
    }

    public NpcLocation getLocation() {
        return location;
    }

    public NpcLocation getLocation(Player player) {
        if (playerLookMap.containsKey(player.getUniqueId())) {
            float[] rotation = playerLookMap.get(player.getUniqueId());
            return location.withRotation(rotation[0], rotation[1]);
        }
        return location;
    }

    public @Nullable Location getBukkitLocation() {
        World world = getWorld();
        if (world == null) return null;
        return location.toBukkitLocation(world);
    }

    public void setLocation(NpcLocation location) {
        this.location = location;
        playerLookMap.clear();
        playerLookMap.putAll(getViewers().stream().collect(Collectors.toMap(Player::getUniqueId, player -> new float[]{location.getYaw(), location.getPitch()})));
        entity.setLocation(location);
        hologram.setLocation(location.withY(location.getY() + type.getHologramOffset()));
    }

    public void setHeadRotation(Player player, float yaw, float pitch) {
        if (getHeadYaw(player) == yaw && getHeadPitch(player) == pitch) return;
        playerLookMap.put(player.getUniqueId(), new float[]{yaw, pitch});
        entity.setHeadRotation(player, yaw, pitch);
    }

    public void setHeadRotation(float yaw, float pitch) {
        for (Player player : getViewers()) {
            if (getHeadYaw(player) == yaw && getHeadPitch(player) == pitch) continue;
            playerLookMap.put(player.getUniqueId(), new float[]{yaw, pitch});
            entity.setHeadRotation(player, yaw, pitch);
        }
    }

    public float getHeadYaw(Player player) {
        return playerLookMap.getOrDefault(player.getUniqueId(), new float[]{location.getYaw(), location.getPitch()})[0];
    }

    public float getHeadPitch(Player player) {
        return playerLookMap.getOrDefault(player.getUniqueId(), new float[]{location.getYaw(), location.getPitch()})[1];
    }

    public HologramImpl getHologram() {
        return hologram;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) delete();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UUID getUuid() {
        return uuid;
    }

    public @Nullable World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public String getWorldName() {
        return worldName;
    }

    @Override
    protected CompletableFuture<Void> UNSAFE_show(Player player) {
        playerLookMap.put(player.getUniqueId(), new float[]{location.getYaw(), location.getPitch()});
        return CompletableFuture.allOf(entity.spawn(player), hologram.show(player));
    }

    @Override
    protected void UNSAFE_hide(Player player) {
        playerLookMap.remove(player.getUniqueId());
        entity.despawn(player);
        hologram.hide(player);
    }

    private <T> void UNSAFE_refreshProperty(EntityPropertyImpl<T> property) {
        if (!type.isAllowedProperty(property)) return;
        for (Player viewer : getViewers()) {
            List<EntityData<?>> data = property.applyStandalone(viewer, entity, true);
            if (!data.isEmpty()) packetFactory.sendMetadata(viewer, entity, data);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(EntityProperty<T> key) {
        return hasProperty(key) ? (T) propertyMap.get((EntityPropertyImpl<?>) key) : key.getDefaultValue();
    }

    public boolean hasProperty(EntityProperty<?> key) {
        return propertyMap.containsKey((EntityPropertyImpl<?>) key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void setProperty(EntityProperty<T> key, T value) {
        // See https://github.com/Pyrbu/ZNPCsPlus/pull/129#issuecomment-1948777764
        Object val = value;
        if (val instanceof ItemStack) val = SpigotConversionUtil.fromBukkitItemStack((ItemStack) val);

        setProperty((EntityPropertyImpl<T>) key, (T) val);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setItemProperty(EntityProperty<?> key, ItemStack value) {
        setProperty((EntityPropertyImpl<com.github.retrooper.packetevents.protocol.item.ItemStack>) key, SpigotConversionUtil.fromBukkitItemStack(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ItemStack getItemProperty(EntityProperty<?> key) {
        return SpigotConversionUtil.toBukkitItemStack(getProperty((EntityProperty<com.github.retrooper.packetevents.protocol.item.ItemStack>) key));
    }

    public <T> void setProperty(EntityPropertyImpl<T> key, T value) {
        if (key == null) return;
        if (value == null || value.equals(key.getDefaultValue())) propertyMap.remove(key);
        else propertyMap.put(key, value);
        UNSAFE_refreshProperty(key);
    }

    @SuppressWarnings("unchecked")
    public <T> void UNSAFE_setProperty(EntityPropertyImpl<?> property, Object value) {
        setProperty((EntityPropertyImpl<T>) property, (T) value);
    }

    @SuppressWarnings("unchecked")
    public <T> void UNSAFE_setProperty(EntityProperty<?> property, Object value) {
        setProperty((EntityPropertyImpl<T>) property, (T) value);
    }

    public Set<EntityProperty<?>> getAllProperties() {
        return Collections.unmodifiableSet(propertyMap.keySet());
    }

    @Override
    public Set<EntityProperty<?>> getAppliedProperties() {
        return Collections.unmodifiableSet(propertyMap.keySet()).stream().filter(type::isAllowedProperty).collect(Collectors.toSet());
    }

    @Override
    public List<InteractionAction> getActions() {
        return Collections.unmodifiableList(actions);
    }

    @Override
    public void removeAction(int index) {
        actions.remove(index);
    }

    @Override
    public void addAction(InteractionAction action) throws IllegalArgumentException {
        if (action == null) throw new IllegalArgumentException("action can not be null");
        actions.add(action);
    }

    @Override
    public void clearActions() {
        actions.clear();
    }

    @Override
    public void editAction(int index, InteractionAction action) throws IllegalArgumentException {
        if (action == null) throw new IllegalArgumentException("action can not be null");
        actions.set(index, action);
    }

    @Override
    public int getPacketEntityId() {
        return entity.getEntityId();
    }

    public void setWorld(World world) {
        if (world == null) throw new IllegalArgumentException("world can not be null");
        delete();
        this.worldName = world.getName();
    }

    public void setWorld(String name) {
        if (name == null) throw new IllegalArgumentException("world name can not be null");
        delete();
        this.worldName = name;
    }

    public void swingHand(boolean offHand) {
        for (Player viewer : getViewers()) entity.swingHand(viewer, offHand);
    }

    @Override
    public @NotNull List<Integer> getPassengers() {
        return entity.getPassengers();
    }

    @Override
    public void addPassenger(int entityId) {
        entity.addPassenger(entityId);
    }

    @Override
    public void removePassenger(int entityId) {
        entity.removePassenger(entityId);
    }

    @Override
    public @Nullable Integer getVehicleId() {
        return entity.getVehicleId();
    }

    @Override
    public void setVehicleId(Integer vehicleId) {
        entity.setVehicleId(vehicleId);
    }
}
