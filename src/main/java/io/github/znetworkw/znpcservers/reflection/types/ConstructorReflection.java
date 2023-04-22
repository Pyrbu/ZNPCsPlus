package io.github.znetworkw.znpcservers.reflection.types;

import com.google.common.collect.ImmutableList;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;
import io.github.znetworkw.znpcservers.reflection.ReflectionLazyLoader;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.function.Consumer;

public class ConstructorReflection extends ReflectionLazyLoader<Constructor<?>> {
    private final ImmutableList<Class<?>[]> parameterTypes;

    public ConstructorReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
        this.parameterTypes = reflectionBuilder.getParameterTypes();
    }

    protected Constructor<?> load() throws NoSuchMethodException {
        for (Class<?> clazz : this.reflectionClasses) {
            Constructor<?> constructor = load(clazz);
            if (constructor != null) return constructor;
        }
        return null;
    }

    private Constructor<?> load(Class<?> clazz) {
        if (parameterTypes != null && parameterTypes.size() > 0) for (Class<?>[] possibleConstructor : parameterTypes) try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(possibleConstructor);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException ignored) {}
        else try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException ignored) {}
        return null;
    }

    @Override
    protected void printDebugInfo(Consumer<String> logger) {
        logger.accept("Possible Parameter Type Combinations:");
        for (Class<?>[] possible : parameterTypes) logger.accept(Arrays.toString(possible));
    }
}