package io.github.znetworkw.znpcservers.npc;

import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.reflection.ReflectionBuilder;
import io.github.znetworkw.znpcservers.reflection.ReflectionPackage;
import io.github.znetworkw.znpcservers.reflection.types.EnumReflection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CustomizationLoader {
    private final Class<? extends Entity> entityClass;
    private final Map<String, Method> methods;

    public CustomizationLoader(EntityType entityType, Iterable<String> methodsName) {
        this(entityType.getEntityClass(), methodsName);
    }

    protected CustomizationLoader(Class<? extends Entity> entityClass, Iterable<String> methodsName) {
        this.entityClass = entityClass;
        this.methods = loadMethods(methodsName);
    }

    protected Map<String, Method> loadMethods(Iterable<String> iterable) {
        Map<String, Method> builder = new HashMap<>();
        for (Method method : this.entityClass.getMethods()) {
            if (builder.containsKey(method.getName()) || !Iterables.contains(iterable, method.getName())) continue;
            for (Class<?> parameter : method.getParameterTypes()) {
                PrimitivePropertyType primitivePropertyType = PrimitivePropertyType.forType(parameter);
                if (primitivePropertyType != null || !parameter.isEnum()) continue;
                new EnumReflection(new ReflectionBuilder(ReflectionPackage.MINECRAFT).withClassName(parameter)).get();
            }
            builder.put(method.getName(), method);
        }
        return builder;
    }

    public boolean contains(String name) {
        return this.methods.containsKey(name);
    }

    public Map<String, Method> getMethods() {
        return this.methods;
    }
}
