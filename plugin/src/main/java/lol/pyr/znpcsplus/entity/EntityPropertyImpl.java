package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntityPropertyImpl<T> implements EntityProperty<T> {
    private final String name;
    private final T defaultValue;
    private final Class<T> clazz;

    protected EntityPropertyImpl(String name, T defaultValue, Class<T> clazz) {
        this.name = name.toLowerCase();
        this.defaultValue = defaultValue;
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    public Class<T> getType() {
        return clazz;
    }

    protected static <V> EntityData newEntityData(int index, EntityDataType<V> type, V value) {
        return new EntityData(index, type, value);
    }

    public List<EntityData> makeStandaloneData(T value, Player player, PacketEntity packetEntity, boolean isSpawned) {
        Map<Integer, EntityData> map = new HashMap<>();
        apply(value, player, packetEntity, isSpawned, map);
        return new ArrayList<>(map.values());
    }

    abstract public void apply(T value, Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties);

    @SuppressWarnings("unchecked")
    public void UNSAFE_update(Object value, Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        apply((T) value, player, entity, isSpawned, properties);
    }
}