package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class DummyProperty<T> extends EntityPropertyImpl<T> {
    @SuppressWarnings("unchecked")
    public DummyProperty(String name, T defaultValue) {
        super(name, defaultValue, (Class<T>) defaultValue.getClass());
    }

    public DummyProperty(String name, Class<T> clazz) {
        super(name, null, clazz);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
    }
}
