package lol.pyr.znpcsplus.entity;

import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;

public class EntityPropertyImpl<T> implements EntityProperty<T> {
    private final String name;
    private final T defaultValue;
    private final Class<T> clazz;
    private final PropertySerializer<T> serializer;

    protected EntityPropertyImpl(String name, T defaultValue, Class<T> clazz, PropertySerializer<T> serializer) {
        this.name = name.toLowerCase();
        this.defaultValue = defaultValue;
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    public String getName() {
        return name;
    }

    public String serialize(PropertyHolder holder) {
        return serialize(holder.getProperty(this));
    }

    public String serialize(T value) {
        return serializer.serialize(value);
    }

    public T deserialize(String str) {
        return serializer.deserialize(str);
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    public Class<T> getType() {
        return clazz;
    }
}