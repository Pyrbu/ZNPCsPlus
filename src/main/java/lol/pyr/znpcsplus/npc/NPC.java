package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.entity.PacketLocation;
import lol.pyr.znpcsplus.entity.PacketPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NPC {
    private final Set<Player> viewers = new HashSet<>();
    private final String worldName;
    private PacketEntity entity;
    private PacketLocation location;
    private NPCType type;

    private final Map<NPCPropertyKey<?>, Object> propertyMap = new HashMap<>();

    public NPC(World world, NPCType type, PacketLocation location) {
        this.worldName = world.getName();
        this.type = type;
        this.location = location;
        entity = new PacketEntity(type.getType(), location);
    }

    public void setType(NPCType type) {
        _hideAll();
        this.type = type;
        entity = type.getType() == EntityTypes.PLAYER ? new PacketPlayer(entity.getLocation()) : new PacketEntity(type.getType(), entity.getLocation());
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
        _hideAll();
        viewers.clear();
    }

    public void respawn() {
        _hideAll();
        _showAll();
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
    public <T> T getProperty(NPCPropertyKey<T> key) {
        return (T) propertyMap.get(key);
    }

    public boolean hasProperty(NPCPropertyKey<?> key) {
        return propertyMap.containsKey(key);
    }

    public <T> void setProperty(NPCPropertyKey<T> key, T value) {
        propertyMap.put(key, value);
        key.update(this, value);
    }

    public void removeProperty(NPCPropertyKey<?> key) {
        propertyMap.remove(key);
    }
}
