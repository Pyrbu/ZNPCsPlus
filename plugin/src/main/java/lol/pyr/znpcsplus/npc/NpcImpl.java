package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.interaction.InteractionActionImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NpcLocation;
import lol.pyr.znpcsplus.util.Viewable;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
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
    private final List<InteractionActionImpl> actions = new ArrayList<>();

    protected NpcImpl(UUID uuid, EntityPropertyRegistryImpl propertyRegistry, ConfigManager configManager, LegacyComponentSerializer textSerializer, World world, NpcTypeImpl type, NpcLocation location, PacketFactory packetFactory) {
        this(uuid, propertyRegistry, configManager, packetFactory, textSerializer, world.getName(), type, location);
    }

    public NpcImpl(UUID uuid, EntityPropertyRegistryImpl propertyRegistry, ConfigManager configManager, PacketFactory packetFactory, LegacyComponentSerializer textSerializer, String world, NpcTypeImpl type, NpcLocation location) {
        this.packetFactory = packetFactory;
        this.worldName = world;
        this.type = type;
        this.location = location;
        this.uuid = uuid;
        entity = new PacketEntity(packetFactory, this, type.getType(), location);
        hologram = new HologramImpl(propertyRegistry, configManager, packetFactory, textSerializer, location.withY(location.getY() + type.getHologramOffset()));
    }

    public void setType(NpcTypeImpl type) {
        UNSAFE_hideAll();
        this.type = type;
        entity = new PacketEntity(packetFactory, this, type.getType(), entity.getLocation());
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

    public Location getBukkitLocation() {
        return location.toBukkitLocation(getWorld());
    }

    public void setLocation(NpcLocation location) {
        this.location = location;
        entity.setLocation(location, getViewers());
        hologram.setLocation(location.withY(location.getY() + type.getHologramOffset()));
    }

    public void setHeadRotation(Player player, float yaw, float pitch) {
        entity.setHeadRotation(player, yaw, pitch);
    }

    public void setHeadRotation(float yaw, float pitch) {
        for (Player player : getViewers()) {
            entity.setHeadRotation(player, yaw, pitch);
        }
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

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public String getWorldName() {
        return worldName;
    }

    @Override
    protected void UNSAFE_show(Player player) {
        entity.spawn(player);
        hologram.show(player);
    }

    @Override
    protected void UNSAFE_hide(Player player) {
        entity.despawn(player);
        hologram.hide(player);
    }

    private <T> void UNSAFE_refreshProperty(EntityPropertyImpl<T> property) {
        for (Player viewer : getViewers()) {
            List<EntityData> data = property.applyStandalone(viewer, entity, true);
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

    @Override
    public <T> void setProperty(EntityProperty<T> key, T value) {
        setProperty((EntityPropertyImpl<T>) key, value );
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

    public Set<EntityProperty<?>> getAllProperties() {
        return Collections.unmodifiableSet(propertyMap.keySet());
    }

    @Override
    public Set<EntityProperty<?>> getAppliedProperties() {
        return Collections.unmodifiableSet(propertyMap.keySet()).stream().filter(type::isAllowedProperty).collect(Collectors.toSet());
    }

    public List<InteractionActionImpl> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public void removeAction(int index) {
        actions.remove(index);
    }

    public void clearActions() {
        actions.clear();
    }

    public void addAction(InteractionActionImpl action) {
        actions.add(action);
    }

    public void editAction(int index, InteractionActionImpl action) {
        actions.set(index, action);
    }

    @Override
    public int getPacketEntityId() {
        return entity.getEntityId();
    }

    public void setWorld(World world) {
        delete();
        this.worldName = world.getName();
    }
}
