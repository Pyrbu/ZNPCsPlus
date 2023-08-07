package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class BooleanProperty extends EntityPropertyImpl<Boolean> {
    private final int index;
    private final boolean legacy;

    public BooleanProperty(String name, int index, boolean defaultValue, boolean legacy) {
        super(name, defaultValue, Boolean.class);
        this.index = index;
        this.legacy = legacy;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        if (legacy) properties.put(index, new EntityData(index, EntityDataTypes.BYTE, (entity.getProperty(this) ? 1 : 0)));
        else properties.put(index, new EntityData(index, EntityDataTypes.BOOLEAN, entity.getProperty(this)));
    }
}
