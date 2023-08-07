package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class ShakingProperty extends EntityPropertyImpl<Boolean> {
    private final int index;

    public ShakingProperty(int index) {
        super("shaking", false, Boolean.class);
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        properties.put(index, newEntityData(index, EntityDataTypes.INT, entity.getProperty(this) ? 140 : 0));
    }
}
