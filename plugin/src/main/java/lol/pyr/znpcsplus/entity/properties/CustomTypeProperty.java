package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class CustomTypeProperty<T, U> extends EntityPropertyImpl<T> {
    private final int index;
    private final EntityDataType<U> type;
    private final TypeDecoder<T, U> decoder;

    @SuppressWarnings("unchecked")
    public CustomTypeProperty(String name, int index, T def, EntityDataType<U> type, TypeDecoder<T, U> decoder) {
        super(name, def, (Class<T>) def.getClass());
        this.index = index;
        this.type = type;
        this.decoder = decoder;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        properties.put(index, newEntityData(index, type, decoder.decode(entity.getProperty(this))));
    }

    public interface TypeDecoder<T, U> {
        U decode(T obj);
    }
}
