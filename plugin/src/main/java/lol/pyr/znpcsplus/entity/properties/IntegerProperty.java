package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class IntegerProperty extends EntityPropertyImpl<Integer> {
    private final int index;
    private final boolean legacy;

    public IntegerProperty(String name, int index, Integer defaultValue) {
        this(name, index, defaultValue, false);
    }

    public IntegerProperty(String name, int index, Integer defaultValue, boolean legacy) {
        super(name, defaultValue, Integer.class);
        this.index = index;
        this.legacy = legacy;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        properties.put(index, legacy ?
                newEntityData(index, EntityDataTypes.BYTE, (byte) entity.getProperty(this).intValue()) :
                newEntityData(index, EntityDataTypes.INT, entity.getProperty(this)));
    }
}
