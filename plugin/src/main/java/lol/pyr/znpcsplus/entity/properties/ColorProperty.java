package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Map;

public class ColorProperty extends EntityPropertyImpl<Color> {
    private final int index;

    public ColorProperty(String name, int index, Color def) {
        super(name, def, Color.class);
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        Color color = entity.getProperty(this);
        if (color == null) return;
        properties.put(index, newEntityData(index, EntityDataTypes.INT, color.asRGB()));
    }
}
