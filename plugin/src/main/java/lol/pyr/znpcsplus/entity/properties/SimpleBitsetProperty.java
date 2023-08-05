package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class SimpleBitsetProperty extends EntityPropertyImpl<Boolean> {
    private final int index;
    private final int bitmask;
    private final boolean inverted;

    public SimpleBitsetProperty(String name, int index, int bitmask, boolean inverted) {
        super(name, !inverted, Boolean.class);
        this.index = index;
        this.bitmask = bitmask;
        this.inverted = inverted;
    }

    public SimpleBitsetProperty(String name, int index, int bitmask) {
        this(name, index, bitmask, false);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        EntityData oldData = properties.get(index);
        byte oldValue = oldData == null ? 0 : (byte) oldData.getValue();
        boolean enabled = entity.getProperty(this);
        if (inverted) enabled = !enabled;
        properties.put(index, newEntityData(index, EntityDataTypes.BYTE, (byte) (oldValue | (enabled ? bitmask : 0))));
    }
}
