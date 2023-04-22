package io.github.znetworkw.znpcservers.reflection.types;

import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;
import io.github.znetworkw.znpcservers.reflection.ReflectionLazyLoader;

public class ClassReflection extends ReflectionLazyLoader<Class<?>> {
    public ClassReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
    }

    protected Class<?> load() {
        return this.reflectionClasses.size() > 0 ? this.reflectionClasses.get(0) : null;
    }
}