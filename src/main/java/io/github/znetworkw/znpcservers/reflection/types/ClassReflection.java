package io.github.znetworkw.znpcservers.reflection.types;

import io.github.znetworkw.znpcservers.reflection.ReflectionLazyLoader;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

public class ClassReflection extends ReflectionLazyLoader<Class<?>> {
    public ClassReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
    }

    protected Class<?> load() {
        return this.reflectionClass;
    }
}