package lol.pyr.znpcsplus.hologram;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HologramLine<M> implements PropertyHolder {
    private M value;
    private final PacketEntity entity;
    private final Set<EntityProperty<?>> properties;

    public HologramLine(M value, PacketFactory packetFactory, EntityType type, NpcLocation location) {
        this.value = value;
        this.entity = new PacketEntity(packetFactory, this, type, location);
        this.properties = new HashSet<>();
    }

    public M getValue() {
        return value;
    }

    public void setValue(M value) {
        this.value = value;
    }

    public void refreshMeta(Player player) {
        entity.refreshMeta(player);
    }

    protected void show(Player player) {
        entity.spawn(player);
    }

    protected void hide(Player player) {
        entity.despawn(player);
    }

    public void setLocation(NpcLocation location, Collection<Player> viewers) {
        entity.setLocation(location, viewers);
    }

    public int getEntityId() {
        return entity.getEntityId();
    }

    public <T> void addProperty(EntityProperty<T> property) {
        properties.add(property);
    }

    @Override
    public <T> T getProperty(EntityProperty<T> key) {
        return key.getDefaultValue();
    }

    @Override
    public boolean hasProperty(EntityProperty<?> key) {
        return properties.contains(key);
    }

    @Override
    public <T> void setProperty(EntityProperty<T> key, T value) {
        throw new UnsupportedOperationException("Can't set properties on a hologram line");
    }

    @Override
    public void setItemProperty(EntityProperty<?> key, ItemStack value) {
        throw new UnsupportedOperationException("Can't set properties on a hologram line");
    }

    @Override
    public Set<EntityProperty<?>> getAppliedProperties() {
        return properties;
    }
}
