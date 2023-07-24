package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class EffectsProperty extends EntityPropertyImpl<Boolean> {
    private final int bitmask;

    public EffectsProperty(String name, int bitmask) {
        super(name, false, Boolean.class);
        this.bitmask = bitmask;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        EntityData oldData = properties.get(0);
        byte oldValue = oldData == null ? 0 : (byte) oldData.getValue();
        properties.put(0, newEntityData(0, EntityDataTypes.BYTE, (byte) (oldValue | (entity.getProperty(this) ? bitmask : 0))));
    }
}
