package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class EncodedLongProperty<T> extends EntityPropertyImpl<T> {
    private final EntityDataType<Long> type;
    private final LongDecoder<T> decoder;
    private final int index;

    protected EncodedLongProperty(String name, T defaultValue, Class<T> clazz, int index, LongDecoder<T> decoder, EntityDataType<Long> type) {
        super(name, defaultValue, clazz);
        this.decoder = decoder;
        this.index = index;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public EncodedLongProperty(String name, T defaultValue, int index, LongDecoder<T> decoder) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, EntityDataTypes.LONG);
    }

    @SuppressWarnings("unchecked")
    public EncodedLongProperty(String name, T defaultValue, int index, LongDecoder<T> decoder, EntityDataType<Long> type) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, type);
    }

    public EncodedLongProperty(String name, Class<T> clazz, int index, LongDecoder<T> decoder) {
        this(name, null, clazz, index, decoder, EntityDataTypes.LONG);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData<?>> properties) {
        T value = entity.getProperty(this);
        if (value == null) return;
        properties.put(index, newEntityData(index, type, decoder.decode(value)));
    }

    public interface LongDecoder<T> {
        long decode(T obj);
    }
}
