package lol.pyr.znpcsplus.reflection.types;

import lol.pyr.znpcsplus.reflection.ReflectionBuilder;
import lol.pyr.znpcsplus.reflection.ReflectionLazyLoader;

public class ClassReflection extends ReflectionLazyLoader<Class<?>> {
    public ClassReflection(ReflectionBuilder reflectionBuilder) {
        super(reflectionBuilder);
    }

    protected Class<?> load() {
        return this.reflectionClasses.size() > 0 ? this.reflectionClasses.get(0) : null;
    }
}