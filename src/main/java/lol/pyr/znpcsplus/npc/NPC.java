package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.entity.PacketLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class NPC {
    private final Set<Player> viewers = new HashSet<>();
    private final String worldName;
    private PacketEntity entity;
    private PacketLocation location;
    private NPCType type;

    public NPC(World world, NPCType type, PacketLocation location) {
        this.worldName = world.getName();
        this.type = type;
        this.location = location;
        entity = new PacketEntity(type.getType(), location); // TODO: Entity ID Provider
    }

    public void setType(NPCType type) {
        _hideAll();
        this.type = type;
        entity = new PacketEntity(type.getType(), entity.getLocation());
        _showAll();
    }

    public NPCType getType() {
        return type;
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

    public void show(Player player) {
        if (viewers.contains(player)) return;
        _show(player);
        viewers.add(player);
    }

    private void _show(Player player) {
        entity.spawn(player);
    }

    public void hide(Player player) {
        if (!viewers.contains(player)) return;
        _hide(player);
        viewers.remove(player);
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

    public boolean isShown(Player player) {
        return viewers.contains(player);
    }
}
