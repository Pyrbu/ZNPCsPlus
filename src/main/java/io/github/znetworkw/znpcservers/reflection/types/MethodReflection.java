package io.github.znetworkw.znpcservers.reflection.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.reflection.ReflectionLazyLoader;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;

import java.lang.reflect.Method;

public class MethodReflection extends ReflectionLazyLoader<Method> {
    private final ImmutableList<String> methods;
    private final ImmutableList<Class<?>[]> parameterTypes;
    private final Class<?> expectType;

    public MethodReflection(ReflectionBuilder builder) {
        super(builder);
        this.methods = builder.getMethods();
        this.expectType = builder.getExpectType();
        this.parameterTypes = builder.getParameterTypes();
    }

    protected Method load() {
        Method methodThis = null;
        boolean hasExpectedType = (expectType != null);
        if (methods.isEmpty() && hasExpectedType) for (Method method : this.reflectionClass.getDeclaredMethods()) if (method.getReturnType() == expectType) return method;
        for (String methodName : methods) try {
            Method maybeGet;
            if (!Iterables.isEmpty(parameterTypes)) maybeGet = this.reflectionClass.getDeclaredMethod(methodName, Iterables.get(parameterTypes, 0));
            else maybeGet = this.reflectionClass.getDeclaredMethod(methodName);
            if (expectType != null && expectType != maybeGet.getReturnType()) continue;
            maybeGet.setAccessible(true);
            methodThis = maybeGet;
        } catch (NoSuchMethodException ignored) {}
        return methodThis;
    }
}