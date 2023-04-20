package io.github.znetworkw.znpcservers.reflection.types;

import io.github.znetworkw.znpcservers.reflection.BaseReflection;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

public class ClassReflection extends BaseReflection<Class<?>> {
    public ClassReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
    }

    protected Class<?> load() {
        return this.BUILDER_CLASS;
    }
}