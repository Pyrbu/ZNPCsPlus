package io.github.znetworkw.znpcservers.reflection.types;

import io.github.znetworkw.znpcservers.reflection.BaseReflection;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

import java.lang.reflect.Field;
import java.util.List;

public class FieldReflection extends BaseReflection<Field> {
    private final String fieldName;
    private final Class<?> expectType;

    public FieldReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
        this.fieldName = reflectionBuilder.getFieldName();
        this.expectType = reflectionBuilder.getExpectType();
    }

    protected Field load() throws NoSuchFieldException {
        if (expectType != null)
            for (Field field1 : this.BUILDER_CLASS.getDeclaredFields()) {
                if (field1.getType() == expectType) {
                    field1.setAccessible(true);
                    return field1;
                }
            }
        Field field = this.BUILDER_CLASS.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public AsValueField asValueField() {
        return new AsValueField(this, className);
    }

    private static class AsValueField extends BaseReflection<Object> {
        private final FieldReflection fieldReflection;

        public AsValueField(FieldReflection fieldReflection, List<String> className) {
            super(className);
            this.fieldReflection = fieldReflection;
        }

        protected Object load() throws IllegalAccessException, NoSuchFieldException {
            Field field = this.fieldReflection.load();
            return field.get(null);
        }
    }
}