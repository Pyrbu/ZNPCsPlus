package lol.pyr.znpcsplus.entity;

import java.lang.reflect.InvocationTargetException;

public class PrimitivePropertySerializer<T> implements PropertySerializer<T> {
    private final Class<T> clazz;

    public PrimitivePropertySerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String serialize(T property) {
        return String.valueOf(property);
    }

    @Override
    public T deserialize(String property) {
        try {
            return clazz.getConstructor(String.class).newInstance(property);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new NullPointerException("Failed to deserialize property " + property + " of type " + clazz.getName() + "!");
        }
    }

    @Override
    public Class<T> getTypeClass() {
        return clazz;
    }
}
