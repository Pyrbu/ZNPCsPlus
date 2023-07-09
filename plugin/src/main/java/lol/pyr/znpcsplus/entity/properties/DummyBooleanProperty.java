package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class DummyBooleanProperty extends EntityPropertyImpl<Boolean> {
    public DummyBooleanProperty(String name, boolean def) {
        super(name, def, Boolean.class);
    }

    @Override
    public void apply(Boolean value, Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
    }
}
