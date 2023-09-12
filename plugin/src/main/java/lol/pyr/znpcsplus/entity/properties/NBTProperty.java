package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class NBTProperty<T> extends EntityPropertyImpl<T> {
    private final EntityDataType<NBTCompound> type;
    private final NBTDecoder<T> decoder;
    private final int index;

    public NBTProperty(String name, T defaultValue, Class<T> clazz, int index, NBTDecoder<T> decoder, EntityDataType<NBTCompound> type) {
        super(name, defaultValue, clazz);
        this.decoder = decoder;
        this.index = index;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public NBTProperty(String name, T defaultValue, int index, NBTDecoder<T> decoder) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, EntityDataTypes.NBT);
    }

    @SuppressWarnings("unchecked")
    public NBTProperty(String name, T defaultValue, int index, NBTDecoder<T> decoder, EntityDataType<NBTCompound> type) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder, type);
    }

    public NBTProperty(String name, Class<T> clazz, int index, NBTDecoder<T> decoder) {
        this(name, null, clazz, index, decoder, EntityDataTypes.NBT);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        T value = entity.getProperty(this);
        if (value == null) return;
        properties.put(index, newEntityData(index, type, decoder.decode(value)));
    }

    public interface NBTDecoder<T> {
        NBTCompound decode(T obj);
    }
}
