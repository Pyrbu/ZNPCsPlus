package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.npc.NPCType;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.hologram.Hologram;
import lol.pyr.znpcsplus.interaction.NPCAction;
import lol.pyr.znpcsplus.util.Viewable;
import lol.pyr.znpcsplus.util.ZLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class NPC extends Viewable implements lol.pyr.znpcsplus.api.npc.NPC {
    protected static final Set<NPC> _ALL_NPCS = new HashSet<>();

    private final Set<Player> viewers = new HashSet<>();
    private final String worldName;
    private PacketEntity entity;
    private ZLocation location;
    private NPCType type;
    private final Hologram hologram;

    private final Map<EntityProperty<?>, Object> propertyMap = new HashMap<>();
    private final Set<NPCAction> actions = new HashSet<>();

    public NPC(World world, NPCType type, ZLocation location) {
        this.worldName = world.getName();
        this.type = type;
        this.location = location;
        entity = new PacketEntity(this, type.getType(), location);
        hologram = new Hologram(location.withY(location.getY() + type.getHologramOffset()));
        _ALL_NPCS.add(this);
    }

    public void setType(NPCType type) {
        UNSAFE_hideAll();
        this.type = type;
        entity = new PacketEntity(this, type.getType(), entity.getLocation());
        UNSAFE_showAll();
    }

    public NPCType getType() {
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

    public Hologram getHologram() {
        return hologram;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    @Override
    public void delete() {
        _ALL_NPCS.remove(this);
        super.delete();
    }

    public void undelete() {
        _ALL_NPCS.add(this);
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

    public Collection<NPCAction> getActions() {
        return Collections.unmodifiableSet(actions);
    }

    public void addAction(NPCAction action) {
        actions.add(action);
    }
}
