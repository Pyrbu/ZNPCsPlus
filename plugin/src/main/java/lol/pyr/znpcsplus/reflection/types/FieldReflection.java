package lol.pyr.znpcsplus.reflection.types;

import lol.pyr.znpcsplus.reflection.ReflectionLazyLoader;
import lol.pyr.znpcsplus.reflection.ReflectionBuilder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

public class FieldReflection extends ReflectionLazyLoader<Field> {
    private final String fieldName;
    private final Class<?> expectType;

    public FieldReflection(ReflectionBuilder builder) {
        super(builder);
        this.fieldName = builder.getFieldName();
        this.expectType = builder.getExpectType();
    }

    protected Field load() {
        if (fieldName != null && fieldName.length() > 0) for (Class<?> clazz : this.reflectionClasses) {
            Field field = loadByName(clazz);
            if (field != null) return field;
        }
        if (expectType != null) for (Class<?> clazz : this.reflectionClasses) {
            Field field = loadByType(clazz);
            if (field != null) return field;
        }
        return null;
    }

    private Field loadByName(Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (expectType != null && !field.getType().equals(expectType)) return null;
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ignored) {}
        return null;
    }

    private Field loadByType(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) if (field.getType() == expectType) {
            field.setAccessible(true);
            return field;
        }
        return null;
    }

    @Override
    protected void printDebugInfo(Consumer<String> logger) {
        logger.accept("Field Name: " + fieldName);
        logger.accept("Field Type: " + expectType);
    }

    public ValueReflection<Object> toStaticValueLoader() {
        return toStaticValueLoader(Object.class);
    }

    @SuppressWarnings("unused")
    public <T> ValueReflection<T> toStaticValueLoader(Class<T> valueType) {
        return new ValueReflection<>(this, possibleClassNames, null, strict);
    }

    @SuppressWarnings("unused")
    public <T> ValueReflection<T> toValueLoader(Object obj, Class<T> valueType) {
        return new ValueReflection<>(this, possibleClassNames, obj, strict);
    }

    @SuppressWarnings("unused")
    public <T> ValueModifier<T> toStaticValueModifier(Class<T> valueType) {
        return new ValueModifier<>(this, possibleClassNames, null, strict);
    }

    @SuppressWarnings("unused")
    public <T> ValueModifier<T> toValueModifier(Object obj, Class<T> valueType) {
        return new ValueModifier<>(this, possibleClassNames, obj, strict);
    }

    public static class ValueReflection<T> extends ReflectionLazyLoader<T> {
        protected final Object obj;
        protected final FieldReflection fieldReflection;

        private ValueReflection(FieldReflection fieldReflection, List<String> className, Object obj, boolean strict) {
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

    public static class ValueModifier<T> extends ValueReflection<T> {
        private ValueModifier(FieldReflection fieldReflection, List<String> className, Object obj, boolean strict) {
            super(fieldReflection, className, obj, strict);
        }

        @Override
        public T get() {
            try {
                return load();
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        public void set(T value) {
            try {
                fieldReflection.get().set(obj, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}