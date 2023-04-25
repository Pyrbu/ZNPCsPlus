package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.entity.PacketLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NPC {
    protected static final Set<NPC> _ALL_NPCS = new HashSet<>();

    private final Set<Player> viewers = new HashSet<>();
    private final String worldName;
    private PacketEntity entity;
    private PacketLocation location;
    private NPCType type;

    private final Map<NPCProperty<?>, Object> propertyMap = new HashMap<>();

    public NPC(World world, NPCType type, PacketLocation location) {
        this.worldName = world.getName();
        this.type = type;
        this.location = location;
        entity = new PacketEntity(this, type.getType(), location);

        _ALL_NPCS.add(this);
    }

    public void setType(NPCType type) {
        _hideAll();
        this.type = type;
        entity = new PacketEntity(this, type.getType(), entity.getLocation());
        _showAll();
    }

    public NPCType getType() {
        return type;
    }

    public PacketEntity getEntity() {
        return entity;
    }

    public PacketLocation getLocation() {
        return location;
    }

    public void setLocation(PacketLocation location) {
        this.location = location;
        entity.setLocation(location, viewers);
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public void delete() {
        _ALL_NPCS.remove(this);
        _hideAll();
        viewers.clear();
    }

    public void respawn() {
        _hideAll();
        _showAll();
    }

    public void respawn(Player player) {
        _hide(player);
        _show(player);
    }

    public void show(Player player) {
        if (viewers.contains(player)) return;
        _show(player);
        viewers.add(player);
    }

    public void hide(Player player) {
        if (!viewers.contains(player)) return;
        _hide(player);
        viewers.remove(player);
    }

    public boolean isShown(Player player) {
        return viewers.contains(player);
    }

    private void _show(Player player) {
        entity.spawn(player);
    }

    private void _hide(Player player) {
        entity.despawn(player);
    }

    private void _hideAll() {
        for (Player viewer : viewers) _hide(viewer);
    }

    private void _showAll() {
        for (Player viewer : viewers) _show(viewer);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(NPCProperty<T> key) {
        return hasProperty(key) ? (T) propertyMap.get(key) : key.getDefaultValue();
    }

    public boolean hasProperty(NPCProperty<?> key) {
        return propertyMap.containsKey(key);
    }

    public <T> void setProperty(NPCProperty<T> key, T value) {
        if (value.equals(key.getDefaultValue())) removeProperty(key);
        else propertyMap.put(key, value);
    }

    public void removeProperty(NPCProperty<?> key) {
        propertyMap.remove(key);
    }
}
