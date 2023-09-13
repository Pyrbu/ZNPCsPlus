package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class EncodedIntegerProperty<T> extends EntityPropertyImpl<T> {
    private final EntityDataType<Integer> type;
    private final IntegerDecoder<T> decoder;
    private final int index;

    protected EncodedIntegerProperty(String name, T defaultValue, Class<T> clazz, int index, IntegerDecoder<T> decoder, EntityDataType<Integer> type) {
        super(name, defaultValue, clazz);
        this.decoder = decoder;
        this.index = index;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public EncodedIntegerProperty(String name, T defaultValue, int index, IntegerDecoder<T> decoder) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, EntityDataTypes.INT);
    }

    @SuppressWarnings("unchecked")
    public EncodedIntegerProperty(String name, T defaultValue, int index, IntegerDecoder<T> decoder, EntityDataType<Integer> type) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, type);
    }

    public EncodedIntegerProperty(String name, Class<T> clazz, int index, IntegerDecoder<T> decoder) {
        this(name, null, clazz, index, decoder, EntityDataTypes.INT);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        T value = entity.getProperty(this);
        if (value == null) return;
        properties.put(index, newEntityData(index, type, decoder.decode(value)));
    }

    public interface IntegerDecoder<T> {
        int decode(T obj);
    }
}
