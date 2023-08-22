package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class BitsetProperty extends EntityPropertyImpl<Boolean> {
    private final int index;
    private final int bitmask;
    private final boolean inverted;
    private boolean integer = false;

    public BitsetProperty(String name, int index, int bitmask, boolean inverted, boolean integer) {
        this(name, index, bitmask, inverted);
        this.integer = integer;
    }

    public BitsetProperty(String name, int index, int bitmask, boolean inverted) {
        super(name, inverted, Boolean.class);
        this.index = index;
        this.bitmask = bitmask;
        this.inverted = inverted;
    }

    public BitsetProperty(String name, int index, int bitmask) {
        this(name, index, bitmask, false);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        EntityData oldData = properties.get(index);
        boolean enabled = entity.getProperty(this);
        if (inverted) enabled = !enabled;
        properties.put(index,
                integer ? newEntityData(index, EntityDataTypes.INT, (oldData == null ? 0 : (int) oldData.getValue()) | (enabled ? bitmask : 0)) :
                newEntityData(index, EntityDataTypes.BYTE, (byte) ((oldData == null ? 0 : (byte) oldData.getValue()) | (enabled ? bitmask : 0))));

    }
}
