package lol.pyr.znpcsplus.hologram;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.entity.EntityProperty;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.entity.PacketLocation;
import lol.pyr.znpcsplus.entity.PropertyHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HologramLine implements PropertyHolder {
    private Component text;
    private final PacketEntity armorStand;

    public HologramLine(PacketLocation location, Component text) {
        this.text = text;
        armorStand = new PacketEntity(this, EntityTypes.ARMOR_STAND, location);
    }

    public Component getText() {
        return text;
    }

    public void setText(Component text) {
        this.text = text;
    }

    protected void show(Player player) {
        armorStand.spawn(player);
    }

    protected void hide(Player player) {
        armorStand.despawn(player);
    }

    public void setLocation(PacketLocation location, Collection<Player> viewers) {
        armorStand.setLocation(location, viewers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(EntityProperty<T> key) {
        if (key == EntityProperty.INVISIBLE) return (T) Boolean.TRUE;
        if (key == EntityProperty.NAME) return (T) text;
        return key.getDefaultValue();
    }

    @Override
    public boolean hasProperty(EntityProperty<?> key) {
        return key == EntityProperty.NAME || key == EntityProperty.INVISIBLE;
    }
}
