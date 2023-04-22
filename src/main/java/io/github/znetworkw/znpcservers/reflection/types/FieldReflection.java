package io.github.znetworkw.znpcservers.reflection.types;

import io.github.znetworkw.znpcservers.reflection.ReflectionLazyLoader;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

public class FieldReflection extends ReflectionLazyLoader<Field> {
    private final String fieldName;
    private final Class<?> expectType;

    public FieldReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
        this.fieldName = reflectionBuilder.getFieldName();
        this.expectType = reflectionBuilder.getExpectType();
    }

    protected Field load() throws NoSuchFieldException {
        for (Class<?> clazz : this.reflectionClasses) {
            Field field = load(clazz);
            if (field != null) return field;
        }
        return null;
    }

    private Field load(Class<?> clazz) {
        if (expectType != null) for (Field field : clazz.getDeclaredFields()) if (field.getType() == expectType) {
            field.setAccessible(true);
            return field;
        }
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ignored) {}
        return null;
    }

    @Override
    protected void printDebugInfo(Consumer<String> logger) {
        logger.accept("Field Name: " + fieldName);
        logger.accept("Field Type: " + expectType);
    }

    public FieldValueReflection<Object> staticValueLoader() {
        return staticValueLoader(Object.class);
    }

    @SuppressWarnings("unused")
    public <T> FieldValueReflection<T> staticValueLoader(Class<T> valueType) {
        return new FieldValueReflection<>(this, possibleClassNames, null, strict);
    }

    @SuppressWarnings("unused")
    public <T> FieldValueReflection<T> valueLoader(Object obj, Class<T> valueType) {
        return new FieldValueReflection<>(this, possibleClassNames, obj, strict);
    }

    private static class FieldValueReflection<T> extends ReflectionLazyLoader<T> {
        private final Object obj;
        private final FieldReflection fieldReflection;

        public FieldValueReflection(FieldReflection fieldReflection, List<String> className, Object obj, boolean strict) {
            super(className, strict);
            this.obj = obj;
            this.fieldReflection = fieldReflection;
        }

        @SuppressWarnings("unchecked")
        protected T load() throws IllegalAccessException, NoSuchFieldException, ClassCastException {
            return (T) this.fieldReflection.get().get(obj);
        }

        @Override
        protected void printDebugInfo(Consumer<String> logger) {
            fieldReflection.printDebugInfo(logger);
        }
    }
}