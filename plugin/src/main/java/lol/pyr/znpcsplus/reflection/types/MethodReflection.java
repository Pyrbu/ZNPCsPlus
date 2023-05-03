package lol.pyr.znpcsplus.reflection.types;

import com.google.common.collect.ImmutableList;
import lol.pyr.znpcsplus.reflection.ReflectionBuilder;
import lol.pyr.znpcsplus.reflection.ReflectionLazyLoader;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

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
        Map<Integer, List<Method>> imperfectMatches = new HashMap<>();
        for (Class<?> clazz : this.reflectionClasses) {
            Method method = load(clazz, imperfectMatches);
            if (method != null) return method;
        }
        for (int i = 2; i > 0; i--) if (imperfectMatches.containsKey(i)) {
            return imperfectMatches.get(i).get(0);
        }
        return null;
    }

    private Method load(Class<?> clazz, Map<Integer, List<Method>> imperfectMatches) {
        for (Method method : clazz.getDeclaredMethods()) {
            int matches = 0;
            if (expectType != null) {
                if (!method.getReturnType().equals(expectType)) continue;
                matches++;
            }
            if (parameterTypes.size() > 0) out: for (Class<?>[] possible : parameterTypes) {
                if (method.getParameterCount() != possible.length) continue;
                for (int i = 0; i < possible.length; i++) if (!method.getParameterTypes()[i].equals(possible[i])) continue out;
                matches++;
            }
            if (methods.contains(method.getName())) {
                matches++;
            }
            if (matches == 3) return method;
            else imperfectMatches.computeIfAbsent(matches, i -> new ArrayList<>()).add(method);
        }
        return null;
    }

    @Override
    protected void printDebugInfo(Consumer<String> logger) {
        logger.accept("Expected Return Type: " + expectType);
        logger.accept("Possible method names: " + methods);
        logger.accept("Possible Parameter Type Combinations:");
        for (Class<?>[] possible : parameterTypes) logger.accept(Arrays.toString(possible));
    }
}