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
    private final boolean inverted;

    public BooleanProperty(String name, int index, boolean defaultValue, boolean legacy) {
        this(name, index, defaultValue, legacy, false);
    }

    public BooleanProperty(String name, int index, boolean defaultValue, boolean legacy, boolean inverted) {
        super(name, defaultValue, Boolean.class);
        this.index = index;
        this.legacy = legacy;
        this.inverted = inverted;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        boolean enabled = entity.getProperty(this);
        if (inverted) enabled = !enabled;
        if (legacy) properties.put(index, newEntityData(index, EntityDataTypes.BYTE, (byte) (enabled ? 1 : 0)));
        else properties.put(index, newEntityData(index, EntityDataTypes.BOOLEAN, enabled));
    }
}
