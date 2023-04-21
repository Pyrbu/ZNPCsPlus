package io.github.znetworkw.znpcservers.reflection.types;

import io.github.znetworkw.znpcservers.reflection.ReflectionLazyLoader;
import io.github.znetworkw.znpcservers.reflection.EnumPropertyCache;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

public class EnumReflection extends ReflectionLazyLoader<Enum<?>[]> {
    public EnumReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
    }

    protected Enum<?>[] load() {
        Enum<?>[] enums = (Enum<?>[]) this.reflectionClass.getEnumConstants();
        for (Enum<?> enumConstant : enums) EnumPropertyCache.register(enumConstant.name(), enumConstant, this.reflectionClass);
        return enums;
    }
}