package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.api.npc.Npc;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.interaction.NpcAction;
import lol.pyr.znpcsplus.util.Viewable;
import lol.pyr.znpcsplus.util.ZLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class NpcImpl extends Viewable implements Npc {
    private final Set<Player> viewers = new HashSet<>();
    private final String worldName;
    private PacketEntity entity;
    private ZLocation location;
    private NpcType type;
    private final HologramImpl hologram;

    private final Map<EntityProperty<?>, Object> propertyMap = new HashMap<>();
    private final Set<NpcAction> actions = new HashSet<>();

    protected NpcImpl(World world, NpcType type, ZLocation location) {
        this.worldName = world.getName();
        this.type = type;
        this.location = location;
        entity = new PacketEntity(this, type.getType(), location);
        hologram = new HologramImpl(location.withY(location.getY() + type.getHologramOffset()));
    }

    public void setType(NpcType type) {
        UNSAFE_hideAll();
        this.type = type;
        entity = new PacketEntity(this, type.getType(), entity.getLocation());
        UNSAFE_showAll();
    }

    public NpcType getType() {
        return type;
    }

    public PacketEntity getEntity() {
        return entity;
    }

    public ZLocation getLocation() {
        return location;
    }

    public void setLocation(ZLocation location) {
        this.location = location;
        entity.setLocation(location, viewers);
        hologram.setLocation(location.withY(location.getY() + type.getHologramOffset()));
    }

    public HologramImpl getHologram() {
        return hologram;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    protected void _show(Player player) {
        entity.spawn(player);
        hologram.show(player);
    }

    @Override
    protected void _hide(Player player) {
        entity.despawn(player);
        hologram.hide(player);
    }

    private void _refreshMeta() {
        for (Player viewer : viewers) entity.refreshMeta(viewer);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(EntityProperty<T> key) {
        return hasProperty(key) ? (T) propertyMap.get(key) : key.getDefaultValue();
    }

    public boolean hasProperty(EntityProperty<?> key) {
        return propertyMap.containsKey(key);
    }

    public <T> void setProperty(EntityProperty<T> key, T value) {
        if (value.equals(key.getDefaultValue())) removeProperty(key);
        else {
            propertyMap.put(key, value);
            _refreshMeta();
        }
    }

    public void removeProperty(EntityProperty<?> key) {
        propertyMap.remove(key);
        _refreshMeta();
    }

    public Collection<NpcAction> getActions() {
        return Collections.unmodifiableSet(actions);
    }

    public void addAction(NpcAction action) {
        actions.add(action);
    }
}
