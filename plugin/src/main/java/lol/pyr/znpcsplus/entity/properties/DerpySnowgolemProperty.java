package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class DerpySnowgolemProperty extends EntityPropertyImpl<Boolean> {
    private final int index;

    public DerpySnowgolemProperty(int index) {
        super("derpy_snowgolem", false, Boolean.class);
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        properties.put(index, new EntityData(index, EntityDataTypes.BYTE, (byte) (entity.getProperty(this) ? 0x00 : 0x10)));
    }
}
