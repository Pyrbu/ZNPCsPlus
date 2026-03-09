package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class EncodedOptionalIntegerProperty<T> extends EntityPropertyImpl<T> {
    private final EntityDataType<@NotNull Optional<Integer>> type;
    private final OptionalIntegerDecoder<T> decoder;
    private final int index;

    public EncodedOptionalIntegerProperty(String name, T defaultValue, Class<T> clazz, int index, OptionalIntegerDecoder<T> decoder, EntityDataType<@NotNull Optional<Integer>> type) {
        super(name, defaultValue, clazz);
        this.decoder = decoder;
        this.index = index;
        this.type = type;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData<?>> properties) {
        T value = entity.getProperty(this);
        if (value == null) return;
        properties.put(index, newEntityData(index, type, decoder.decode(value)));
    }

    public interface OptionalIntegerDecoder<T> {
        Optional<Integer> decode(T obj);
    }
}
