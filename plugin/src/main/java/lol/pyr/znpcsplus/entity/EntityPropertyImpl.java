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
    private final List<EntityPropertyImpl<?>> dependencies = new ArrayList<>();

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

    public void addDependency(EntityPropertyImpl<?> property) {
        dependencies.add(property);
    }

    protected static <V> EntityData newEntityData(int index, EntityDataType<V> type, V value) {
        return new EntityData(index, type, value);
    }

    public List<EntityData> applyStandalone(Player player, PacketEntity packetEntity, boolean isSpawned) {
        Map<Integer, EntityData> map = new HashMap<>();
        apply(player, packetEntity, isSpawned, map);
        for (EntityPropertyImpl<?> property : dependencies) property.apply(player, packetEntity, isSpawned, map);
        return new ArrayList<>(map.values());
    }

    abstract public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties);
}