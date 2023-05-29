package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.interaction.InteractionAction;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NpcLocation;
import lol.pyr.znpcsplus.util.Viewable;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class NpcImpl extends Viewable implements Npc {
    private final PacketFactory packetFactory;
    private final String worldName;
    private PacketEntity entity;
    private NpcLocation location;
    private NpcTypeImpl type;
    private final HologramImpl hologram;

    private final Map<EntityPropertyImpl<?>, Object> propertyMap = new HashMap<>();
    private final List<InteractionAction> actions = new ArrayList<>();

    protected NpcImpl(ConfigManager configManager, LegacyComponentSerializer textSerializer, World world, NpcTypeImpl type, NpcLocation location, PacketFactory packetFactory) {
        this(configManager, packetFactory, textSerializer, world.getName(), type, location);
    }

    public NpcImpl(ConfigManager configManager, PacketFactory packetFactory, LegacyComponentSerializer textSerializer, String world, NpcTypeImpl type, NpcLocation location) {
        this.packetFactory = packetFactory;
        this.worldName = world;
        this.type = type;
        this.location = location;
        entity = new PacketEntity(packetFactory, this, type.getType(), location);
        hologram = new HologramImpl(configManager, packetFactory, textSerializer, location.withY(location.getY() + type.getHologramOffset()));
    }


    public void setType(NpcTypeImpl type) {
        UNSAFE_hideAll();
        this.type = type;
        entity = new PacketEntity(packetFactory, this, type.getType(), entity.getLocation());
        UNSAFE_showAll();
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

    public HologramImpl getHologram() {
        return hologram;
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

    private void UNSAFE_refreshMeta() {
        for (Player viewer : getViewers()) entity.refreshMeta(viewer);
    }

    private void UNSAFE_remakeTeam() {
        for (Player viewer : getViewers()) entity.remakeTeam(viewer);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(EntityProperty<T> key) {
        return hasProperty(key) ? (T) propertyMap.get((EntityPropertyImpl<?>) key) : key.getDefaultValue();
    }

    public boolean hasProperty(EntityProperty<?> key) {
        return propertyMap.containsKey((EntityPropertyImpl<?>) key);
    }

    public <T> void setProperty(EntityPropertyImpl<T> key, T value) {
        if (value == null || value.equals(key.getDefaultValue())) removeProperty(key);
        else propertyMap.put(key, value);
        UNSAFE_refreshMeta();
        if (key.getName().equalsIgnoreCase("glow")) UNSAFE_remakeTeam();
    }

    @SuppressWarnings("unchecked")
    public <T> void UNSAFE_setProperty(EntityPropertyImpl<?> property, Object value) {
        setProperty((EntityPropertyImpl<T>) property, (T) value);
    }

    public void removeProperty(EntityPropertyImpl<?> key) {
        propertyMap.remove(key);
        UNSAFE_refreshMeta();
        if (key.getName().equalsIgnoreCase("glow")) UNSAFE_remakeTeam();
    }

    public Set<EntityPropertyImpl<?>> getAppliedProperties() {
        return Collections.unmodifiableSet(propertyMap.keySet());
    }

    public List<InteractionAction> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public void removeAction(int index) {
        actions.remove(index);
    }

    public void addAction(InteractionAction action) {
        actions.add(action);
    }

    public void editAction(int index, InteractionAction action) {
        actions.set(index, action);
    }
}
