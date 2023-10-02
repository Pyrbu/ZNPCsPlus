package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.TropicalFishVariant;
import org.bukkit.entity.Player;

import java.util.Map;

public class TropicalFishVariantProperty<T> extends EntityPropertyImpl<T> {
    private final int index;
    private final BuilderDecoder<T> decoder;

    public TropicalFishVariantProperty(String name, T defaultValue, Class<T> type, int index, BuilderDecoder<T> decoder) {
        super(name, defaultValue, type);
        this.index = index;
        this.decoder = decoder;
    }

    @SuppressWarnings("unchecked")
    public TropicalFishVariantProperty(String name, T defaultValue, int index, BuilderDecoder<T> decoder) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), index, decoder);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        T value = entity.getProperty(this);
        if (value == null) {
            return;
        }
        EntityData oldData = properties.get(index);
        TropicalFishVariant.Builder builder;
        if (oldData != null && oldData.getType() == EntityDataTypes.INT && oldData.getValue() != null) {
            int oldVal = (int) oldData.getValue();
            builder = TropicalFishVariant.Builder.fromInt(oldVal);
        } else {
            builder = new TropicalFishVariant.Builder();
        }
        builder = decoder.decode(builder, value);
        int variant = builder.build().getVariant();
        properties.put(index, newEntityData(index, EntityDataTypes.INT, variant));
    }

    public interface BuilderDecoder<T> {
        TropicalFishVariant.Builder decode(TropicalFishVariant.Builder builder, T obj);
    }
}
