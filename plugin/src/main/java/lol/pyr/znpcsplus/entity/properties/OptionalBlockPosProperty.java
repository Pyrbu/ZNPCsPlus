package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.Vector3i;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class OptionalBlockPosProperty extends EntityPropertyImpl<Vector3i> {
    private final int index;

    public OptionalBlockPosProperty(String name, Vector3i defaultValue, int index) {
        super(name, defaultValue, Vector3i.class);
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        Vector3i value = entity.getProperty(this);
        if (value == null) properties.put(index, new EntityData(index, EntityDataTypes.OPTIONAL_BLOCK_POSITION, Optional.empty()));
        else properties.put(index, new EntityData(index, EntityDataTypes.OPTIONAL_BLOCK_POSITION,
                Optional.of(new com.github.retrooper.packetevents.util.Vector3i(value.getX(), value.getY(), value.getZ()))));
    }
}
