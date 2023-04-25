package io.github.znetworkw.znpcservers.reflection;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.reflection.types.ClassReflection;
import io.github.znetworkw.znpcservers.reflection.types.FieldReflection;
import io.github.znetworkw.znpcservers.reflection.types.MethodReflection;
import io.github.znetworkw.znpcservers.utility.Utils;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class containing all of the lazy-loaded reflections that the plugin
 * uses to accessinaccessible things from the server jar.
 */
public final class Reflections {
    public static final Class<?> ENTITY_CLASS = new ClassReflection(new ReflectionBuilder(ReflectionPackage.ENTITY)
            .withClassName("Entity")).get();

    public static final Class<?> ENTITY_HUMAN_CLASS = new ClassReflection(new ReflectionBuilder(ReflectionPackage.ENTITY)
            .withSubClass("player")
            .withClassName("EntityHuman")).get();

    public static final ReflectionLazyLoader<Method> GET_PROFILE_METHOD = new MethodReflection(new ReflectionBuilder(ReflectionPackage.ENTITY)
            .withClassName(ENTITY_HUMAN_CLASS)
            .withExpectResult(GameProfile.class));

    public static final ReflectionLazyLoader<Method> GET_HANDLE_PLAYER_METHOD = new MethodReflection(new ReflectionBuilder(ReflectionPackage.BUKKIT)
            .withClassName("entity.CraftPlayer").withClassName("entity.CraftHumanEntity")
            .withMethodName("getHandle"));

    public static final FieldReflection.ValueModifier<Integer> ENTITY_ID_MODIFIER = new FieldReflection(new ReflectionBuilder(ReflectionPackage.ENTITY)
            .withClassName(ENTITY_CLASS)
            .withFieldName("entityCount")
            .setStrict(!Utils.versionNewer(14))).staticValueModifier(int.class);

    public static final ReflectionLazyLoader<AtomicInteger> ATOMIC_ENTITY_ID_FIELD = new FieldReflection(new ReflectionBuilder(ReflectionPackage.ENTITY)
            .withClassName(ENTITY_CLASS)
            .withFieldName("entityCount")
            .withFieldName("d")
            .withFieldName("c")
            .withExpectResult(AtomicInteger.class)
            .setStrict(Utils.versionNewer(14))).staticValueLoader(AtomicInteger.class);
}
