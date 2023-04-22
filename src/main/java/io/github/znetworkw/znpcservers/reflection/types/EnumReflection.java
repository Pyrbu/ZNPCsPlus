package io.github.znetworkw.znpcservers.reflection.types;

import io.github.znetworkw.znpcservers.reflection.EnumPropertyCache;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;
import io.github.znetworkw.znpcservers.reflection.ReflectionLazyLoader;

public class EnumReflection extends ReflectionLazyLoader<Enum<?>[]> {
    public EnumReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
    }

    protected Enum<?>[] load() {
        if (reflectionClasses.size() == 0) return null;
        Enum<?>[] enums = (Enum<?>[]) this.reflectionClasses.get(0).getEnumConstants();
        for (Enum<?> enumConstant : enums) EnumPropertyCache.register(enumConstant.name(), enumConstant, this.reflectionClasses.get(0));
        return enums;
    }
}