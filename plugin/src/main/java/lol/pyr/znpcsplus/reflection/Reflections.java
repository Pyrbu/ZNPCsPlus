package lol.pyr.znpcsplus.reflection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import lol.pyr.znpcsplus.reflection.types.FieldReflection;
import lol.pyr.znpcsplus.util.FoliaUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Class containing getAll of the lazy-loaded reflections that the plugin
 * uses to access inaccessible components of the server jar.
 */
public final class Reflections {

    /*
     * Game profile methods used for obtaining raw skin data of online players
     */

    public static final Class<?> GAME_PROFILE_CLASS =
            new ReflectionBuilder()
                    .withRawClassName("com.mojang.authlib.GameProfile")
                    .toClassReflection().get();

    public static final Class<?> ENTITY_HUMAN_CLASS =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withSubClass("player")
                    .withClassName("EntityHuman")
                    .toClassReflection().get();

    public static final ReflectionLazyLoader<Method> GET_PLAYER_HANDLE_METHOD =
            new ReflectionBuilder(ReflectionPackage.BUKKIT)
                    .withClassName("entity.CraftPlayer")
                    .withClassName("entity.CraftHumanEntity")
                    .withMethodName("getHandle")
                    .withExpectResult(ENTITY_HUMAN_CLASS)
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> GET_PROFILE_METHOD =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withClassName(ENTITY_HUMAN_CLASS)
                    .withExpectResult(GAME_PROFILE_CLASS)
                    .toMethodReflection();

    public static final Class<?> PROPERTY_MAP_CLASS =
            new ReflectionBuilder()
                    .withRawClassName("com.mojang.authlib.properties.PropertyMap")
                    .toClassReflection().get();

    public static final ReflectionLazyLoader<Method> GET_PROPERTY_MAP_METHOD =
            new ReflectionBuilder(GAME_PROFILE_CLASS)
                    .withMethodName("getProperties")
                    .withExpectResult(PROPERTY_MAP_CLASS)
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> PROPERTY_MAP_VALUES_METHOD =
            new ReflectionBuilder()
                    .withClassName(PROPERTY_MAP_CLASS.getSuperclass())
                    .withMethodName("values")
                    .withExpectResult(Collection.class)
                    .toMethodReflection();

    public static final Class<?> PROPERTY_CLASS =
            new ReflectionBuilder()
                    .withRawClassName("com.mojang.authlib.properties.Property")
                    .toClassReflection().get();

    private static final boolean v1_20_2 = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2);

    public static final ReflectionLazyLoader<Method> PROPERTY_GET_NAME_METHOD =
            new ReflectionBuilder(PROPERTY_CLASS)
                    .withMethodName("getName")
                    .withExpectResult(String.class)
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Field> PROPERTY_NAME_FIELD =
            new ReflectionBuilder(PROPERTY_CLASS)
                    .withFieldName("name")
                    .withExpectResult(String.class)
                    .setStrict(v1_20_2)
                    .toFieldReflection();

    public static final ReflectionLazyLoader<Method> PROPERTY_GET_VALUE_METHOD =
            new ReflectionBuilder(PROPERTY_CLASS)
                    .withMethodName("getValue")
                    .withExpectResult(String.class)
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Field> PROPERTY_VALUE_FIELD =
            new ReflectionBuilder(PROPERTY_CLASS)
                    .withFieldName("value")
                    .withExpectResult(String.class)
                    .setStrict(v1_20_2)
                    .toFieldReflection();

    public static final ReflectionLazyLoader<Method> PROPERTY_GET_SIGNATURE_METHOD =
            new ReflectionBuilder(PROPERTY_CLASS)
                    .withMethodName("getSignature")
                    .withExpectResult(String.class)
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Field> PROPERTY_SIGNATURE_FIELD =
            new ReflectionBuilder(PROPERTY_CLASS)
                    .withFieldName("signature")
                    .withExpectResult(String.class)
                    .setStrict(v1_20_2)
                    .toFieldReflection();

    /*
     * These methods are used for reserving entity ids so regular Minecraft
     * entity packets don't interfere with our packet-based entities
     */

    public static final Class<?> ENTITY_CLASS =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withClassName("Entity")
                    .toClassReflection().get();
    public static final FieldReflection.ValueModifier<Integer> ENTITY_ID_MODIFIER =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withClassName(ENTITY_CLASS)
                    .withFieldName("entityCount")
                    .setStrict(!PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14))
                    .toFieldReflection()
                    .toStaticValueModifier(int.class);

    public static final ReflectionLazyLoader<AtomicInteger> ATOMIC_ENTITY_ID_FIELD =
            new ReflectionBuilder(ReflectionPackage.ENTITY)
                    .withClassName(ENTITY_CLASS)
                    .withFieldName("entityCount")
                    .withFieldName("d")
                    .withFieldName("c")
                    .withExpectResult(AtomicInteger.class)
                    .setStrict(PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14))
                    .toFieldReflection()
                    .toStaticValueLoader(AtomicInteger.class);

    /*
     * All of these folia methods need to be reflected because folia is strictly
     * available on the newest java versions but we need to keep support for Java 8
     */

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

    public static final Class<?> REGION_SCHEDULER_CLASS =
            new ReflectionBuilder("io.papermc.paper.threadedregions.scheduler")
                    .withClassName("RegionScheduler")
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

    public static final ReflectionLazyLoader<Method> FOLIA_GET_REGION_SCHEDULER =
            new ReflectionBuilder(Bukkit.class)
                    .withMethodName("getRegionScheduler")
                    .withExpectResult(REGION_SCHEDULER_CLASS)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_RUN_NOW_ASYNC =
            new ReflectionBuilder(ASYNC_SCHEDULER_CLASS)
                    .withMethodName("runNow")
                    .withParameterTypes(Plugin.class, Consumer.class)
                    .withExpectResult(SCHEDULED_TASK_CLASS)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_RUN_DELAYED_ASYNC =
            new ReflectionBuilder(ASYNC_SCHEDULER_CLASS)
                    .withMethodName("runDelayed")
                    .withParameterTypes(Plugin.class, Consumer.class, long.class, TimeUnit.class)
                    .withExpectResult(SCHEDULED_TASK_CLASS)
                    .setStrict(FoliaUtil.isFolia())
                    .toMethodReflection();

    public static final ReflectionLazyLoader<Method> FOLIA_RUN_AT_FIXED_RATE_ASYNC =
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

    public static final ReflectionLazyLoader<Method> FOLIA_EXECUTE_REGION =
            new ReflectionBuilder(REGION_SCHEDULER_CLASS)
                    .withMethodName("execute")
                    .withParameterTypes(Plugin.class, Location.class, Runnable.class)
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
