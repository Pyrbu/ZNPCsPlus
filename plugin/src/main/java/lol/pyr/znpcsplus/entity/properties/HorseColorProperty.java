package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.HorseColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class HorseColorProperty extends EntityPropertyImpl<HorseColor> {
    private final int index;

    public HorseColorProperty(int index) {
        super("horse_color", HorseColor.WHITE, HorseColor.class);
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        EntityData oldData = properties.get(index);
        HorseColor value = entity.getProperty(this);
        properties.put(index, newEntityData(index, EntityDataTypes.INT, value.ordinal() | (oldData == null ? 0 : ((int) oldData.getValue() & 0xFF00))));
    }
}
