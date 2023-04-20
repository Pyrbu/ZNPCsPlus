package io.github.znetworkw.znpcservers.reflection.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.reflection.BaseReflection;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

import java.lang.reflect.Constructor;

public class ConstructorReflection extends BaseReflection<Constructor<?>> {
    private final ImmutableList<Class<?>[]> parameterTypes;

    public ConstructorReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
        this.parameterTypes = reflectionBuilder.getParameterTypes();
    }

    protected Constructor<?> load() throws NoSuchMethodException {
        Constructor<?> constructor = null;
        if (Iterables.size(parameterTypes) > 1) {
            for (Class<?>[] keyParameters : parameterTypes) try {
                constructor = this.BUILDER_CLASS.getDeclaredConstructor(keyParameters);
            } catch (NoSuchMethodException ignored) {}
        } else constructor = (Iterables.size(parameterTypes) > 0) ? this.BUILDER_CLASS.getDeclaredConstructor(Iterables.get(parameterTypes, 0)) : this.BUILDER_CLASS.getDeclaredConstructor();
        if (constructor != null) constructor.setAccessible(true);
        return constructor;
    }
}