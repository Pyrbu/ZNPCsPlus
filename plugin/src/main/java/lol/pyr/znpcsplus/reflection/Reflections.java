package lol.pyr.znpcsplus.reflection;

import com.mojang.authlib.GameProfile;
import lol.pyr.znpcsplus.reflection.types.FieldReflection;
import lol.pyr.znpcsplus.util.FoliaUtil;
import lol.pyr.znpcsplus.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Class containing all of the lazy-loaded reflections that the plugin
 * uses to access inaccessible components of the server jar.
 */
public final class Reflections {
    public static final Class<?> ENTITY_CLASS =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withClassName("Entity")
                    .toClassReflection().get();

    public static final Class<?> ENTITY_HUMAN_CLASS =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withSubClass("player")
                    .withClassName("EntityHuman")
                    .toClassReflection().get();

    public static final ReflectionLazyLoader<Method> GET_PROFILE_METHOD =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withClassName(ENTITY_HUMAN_CLASS)
                    .withExpectResult(GameProfile.class)
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> GET_HANDLE_PLAYER_METHOD =
            new ReflectionBuilder(ReflectionPackage.BUKKIT)
                    .withClassName("entity.CraftPlayer").withClassName("entity.CraftHumanEntity")
                    .withMethodName("getHandle")
                    .toMethodReflection();

    public static final FieldReflection.ValueModifier<Integer> ENTITY_ID_MODIFIER =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withClassName(ENTITY_CLASS)
                    .withFieldName("entityCount")
                    .setStrict(!VersionUtil.isNewerThan(14))
                    .toFieldReflection()
                    .toStaticValueModifier(int.class);

    public static final ReflectionLazyLoader<AtomicInteger> ATOMIC_ENTITY_ID_FIELD =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withClassName(ENTITY_CLASS)
                    .withFieldName("entityCount")
                    .withFieldName("d")
                    .withFieldName("c")
                    .withExpectResult(AtomicInteger.class)
                    .setStrict(VersionUtil.isNewerThan(14))
                    .toFieldReflection()
                    .toStaticValueLoader(AtomicInteger.class);

    public static final Class<?> ASYNC_SCHEDULER_CLASS =
            new ReflectionBuilder("io.papermc.paper.threadedregions.scheduler")
                    .withClassName("AsyncScheduler")
                    .setStrict(FoliaUtil.isFolia())
                    .toClassReflection().get();

    public static final Class<?> GLOBAL_REGION_SCHEDULER_CLASS =
            new ReflectionBuilder("io.papermc.paper.threadedregions.scheduler")
                    .withClassName("GlobalRegionScheduler")
                    .setStrict(FoliaUtil.isFolia())
                    .toClassReflection().get();

    public static final Class<?> SCHEDULED_TASK_CLASS =
            new ReflectionBuilder("io.papermc.paper.threadedregions.scheduler")
                    .withClassName("ScheduledTask")
                    .setStrict(FoliaUtil.isFolia())
                    .toClassReflection().get();

    public static final ReflectionLazyLoader<Method> FOLIA_GET_ASYNC_SCHEDULER =
            new ReflectionBuilder(Bukkit.class)
                    .withMethodName("getAsyncScheduler")
                    .withExpectResult(ASYNC_SCHEDULER_CLASS)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_GET_GLOBAL_REGION_SCHEDULER =
            new ReflectionBuilder(Bukkit.class)
                    .withMethodName("getGlobalRegionScheduler")
                    .withExpectResult(GLOBAL_REGION_SCHEDULER_CLASS)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_RUN_DELAYED =
            new ReflectionBuilder(ASYNC_SCHEDULER_CLASS)
                    .withMethodName("runDelayed")
                    .withParameterTypes(Plugin.class, Consumer.class, long.class, TimeUnit.class)
                    .withExpectResult(SCHEDULED_TASK_CLASS)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_RUN_AT_FIXED_RATE =
            new ReflectionBuilder(ASYNC_SCHEDULER_CLASS)
                    .withMethodName("runAtFixedRate")
                    .withParameterTypes(Plugin.class, Consumer.class, long.class, long.class, TimeUnit.class)
                    .withExpectResult(SCHEDULED_TASK_CLASS)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_RUN_NOW_GLOBAL =
            new ReflectionBuilder(GLOBAL_REGION_SCHEDULER_CLASS)
                    .withMethodName("runNow")
                    .withParameterTypes(Plugin.class, Consumer.class)
                    .withExpectResult(SCHEDULED_TASK_CLASS)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_CANCEL_ASYNC_TASKS =
            new ReflectionBuilder(ASYNC_SCHEDULER_CLASS)
                    .withMethodName("cancelTasks")
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_CANCEL_GLOBAL_TASKS =
            new ReflectionBuilder(GLOBAL_REGION_SCHEDULER_CLASS)
                    .withMethodName("cancelTasks")
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_TELEPORT_ASYNC =
            new ReflectionBuilder(Entity.class)
                    .withMethodName("teleportAsync")
                    .withParameterTypes(Location.class)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();
}
