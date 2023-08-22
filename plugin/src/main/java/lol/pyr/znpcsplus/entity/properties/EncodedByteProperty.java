package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class EncodedByteProperty<T> extends EntityPropertyImpl<T> {
    private final EntityDataType<Byte> type;
    private final ByteDecoder<T> decoder;
    private final int index;

    protected EncodedByteProperty(String name, T defaultValue, Class<T> clazz, int index, ByteDecoder<T> decoder, EntityDataType<Byte> type) {
        super(name, defaultValue, clazz);
        this.decoder = decoder;
        this.index = index;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public EncodedByteProperty(String name, T defaultValue, int index, ByteDecoder<T> decoder) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, EntityDataTypes.BYTE);
    }

    @SuppressWarnings("unchecked")
    public EncodedByteProperty(String name, T defaultValue, int index, ByteDecoder<T> decoder, EntityDataType<Byte> type) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, type);
    }

    public EncodedByteProperty(String name, Class<T> clazz, int index, ByteDecoder<T> decoder) {
        this(name, null, clazz, index, decoder, EntityDataTypes.BYTE);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        T value = entity.getProperty(this);
        if (value == null) return;
        properties.put(index, newEntityData(index, type, decoder.decode(value)));
    }

    public interface ByteDecoder<T> {
        byte decode(T obj);
    }
}
