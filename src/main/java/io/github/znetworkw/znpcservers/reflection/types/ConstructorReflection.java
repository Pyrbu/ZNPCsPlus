package io.github.znetworkw.znpcservers.reflection.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.reflection.ReflectionLazyLoader;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

import java.lang.reflect.Constructor;

public class ConstructorReflection extends ReflectionLazyLoader<Constructor<?>> {
    private final ImmutableList<Class<?>[]> parameterTypes;

    public ConstructorReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
        this.parameterTypes = reflectionBuilder.getParameterTypes();
    }

    protected Constructor<?> load() throws NoSuchMethodException {
        Constructor<?> constructor = null;
        if (Iterables.size(parameterTypes) > 1) {
            for (Class<?>[] keyParameters : parameterTypes) try {
                constructor = this.reflectionClass.getDeclaredConstructor(keyParameters);
            } catch (NoSuchMethodException ignored) {}
        } else constructor = (Iterables.size(parameterTypes) > 0) ? this.reflectionClass.getDeclaredConstructor(Iterables.get(parameterTypes, 0)) : this.reflectionClass.getDeclaredConstructor();
        if (constructor != null) constructor.setAccessible(true);
        return constructor;
    }
}