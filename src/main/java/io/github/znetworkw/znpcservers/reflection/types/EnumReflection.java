package io.github.znetworkw.znpcservers.reflection.types;

import io.github.znetworkw.znpcservers.reflection.BaseReflection;
import io.github.znetworkw.znpcservers.reflection.ClassCache;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

public class EnumReflection extends BaseReflection<Enum<?>[]> {
    public EnumReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
    }

    protected Enum<?>[] load() {
        Enum<?>[] arrayOfEnum = (Enum<?>[]) this.BUILDER_CLASS.getEnumConstants();
        for (Enum<?> enumConstant : arrayOfEnum) ClassCache.register(enumConstant.name(), enumConstant, this.BUILDER_CLASS);
        return arrayOfEnum;
    }
}