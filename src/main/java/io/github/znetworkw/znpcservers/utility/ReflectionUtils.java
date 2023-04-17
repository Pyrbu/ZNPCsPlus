package io.github.znetworkw.znpcservers.utility;

import java.lang.reflect.Field;

public final class ReflectionUtils {
    public static Field findFieldForClass(Object instance, Class<?> type) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.getType() == type) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    public static Field findFieldForClassAndSet(Object instance, Class<?> type, Object value) throws ReflectiveOperationException {
        Field field = findFieldForClass(instance, type);
        if (field == null)
            return null;
        field.set(instance, value);
        return field;
    }
}
