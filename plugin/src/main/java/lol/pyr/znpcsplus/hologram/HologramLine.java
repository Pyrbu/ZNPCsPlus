package lol.pyr.znpcsplus.hologram;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HologramLine implements PropertyHolder {
    private Component text;
    private final PacketEntity armorStand;

    public HologramLine(PacketFactory packetFactory, NpcLocation location, Component text) {
        this.text = text;
        armorStand = new PacketEntity(packetFactory, this, EntityTypes.ARMOR_STAND, location);
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

    public void setLocation(NpcLocation location, Collection<Player> viewers) {
        armorStand.setLocation(location, viewers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(EntityProperty<T> key) {
        if (key.getName().equalsIgnoreCase("invisible")) return (T) Boolean.TRUE;
        if (key.getName().equalsIgnoreCase("name")) return (T) text;
        return key.getDefaultValue();
    }

    @Override
    public boolean hasProperty(EntityProperty<?> key) {
        return key.getName().equalsIgnoreCase("name") || key.getName().equalsIgnoreCase("invisible");
    }

    @Override
    public <T> void setProperty(EntityProperty<T> key, T value) {
        throw new UnsupportedOperationException("Can't set properties on a hologram");
    }
}
