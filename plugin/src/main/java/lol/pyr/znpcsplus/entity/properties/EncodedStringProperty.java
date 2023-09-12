package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class EncodedStringProperty<T> extends EntityPropertyImpl<T> {
    private final EntityDataType<String> type;
    private final EncodedStringProperty.StringDecoder<T> decoder;
    private final int index;

    public EncodedStringProperty(String name, T defaultValue, Class<T> clazz, int index, StringDecoder<T> decoder, EntityDataType<String> type) {
        super(name, defaultValue, clazz);
        this.decoder = decoder;
        this.index = index;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public EncodedStringProperty(String name, T defaultValue, int index, StringDecoder<T> decoder) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, EntityDataTypes.STRING);
    }

    @SuppressWarnings("unchecked")
    public EncodedStringProperty(String name, T defaultValue, int index, StringDecoder<T> decoder, EntityDataType<String> type) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, type);
    }

    public EncodedStringProperty(String name, Class<T> clazz, int index, StringDecoder<T> decoder) {
        this(name, null, clazz, index, decoder, EntityDataTypes.STRING);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        T value = entity.getProperty(this);
        if (value == null) return;
        properties.put(index, newEntityData(index, type, decoder.decode(value)));
    }

    public interface StringDecoder<T> {
        String decode(T obj);
    }
}
