package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.Vector3f;
import org.bukkit.entity.Player;

import java.util.Map;

public class RotationProperty extends EntityPropertyImpl<Vector3f> {
    private final int index;

    public RotationProperty(String name, int index, Vector3f defaultValue) {
        super(name, defaultValue, Vector3f.class);
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        Vector3f vec = entity.getProperty(this);
        properties.put(index, newEntityData(index, EntityDataTypes.ROTATION, new com.github.retrooper.packetevents.util.Vector3f(vec.getX(), vec.getY(), vec.getZ())));
    }
}
