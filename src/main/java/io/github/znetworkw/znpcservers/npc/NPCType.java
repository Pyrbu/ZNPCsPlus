package io.github.znetworkw.znpcservers.npc;

import io.github.znetworkw.znpcservers.UnexpectedCallException;
import io.github.znetworkw.znpcservers.reflection.EnumPropertyCache;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static io.github.znetworkw.znpcservers.reflection.Reflections.*;

@SuppressWarnings("unused")
public enum NPCType {
    PLAYER(ENTITY_PLAYER_CLASS, 0.0),
    ARMOR_STAND(ENTITY_ARMOR_STAND_CLASS, 0.0, "setSmall", "setArms"),
    CREEPER(ENTITY_CREEPER_CLASS, -0.15, "setPowered"),
    BAT(ENTITY_BAT_CLASS, -0.5, "setAwake"),
    BLAZE(ENTITY_BLAZE_CLASS, 0.0),
    CAVE_SPIDER(ENTITY_CAVE_SPIDER_CLASS, -1.0),
    COW(ENTITY_COW_CLASS, -0.25, "setAge"),
    CHICKEN(ENTITY_CHICKEN_CLASS, -1.0, "setAge"),
    ENDER_DRAGON(ENTITY_ENDER_DRAGON_CLASS, 1.5),
    ENDERMAN(ENTITY_ENDERMAN_CLASS, 0.7),
    ENDERMITE(ENTITY_ENDERMITE_CLASS, -1.5),
    GHAST(ENTITY_GHAST_CLASS, 3.0),
    IRON_GOLEM(ENTITY_IRON_GOLEM_CLASS, 0.75),
    GIANT(ENTITY_GIANT_ZOMBIE_CLASS, 11.0),
    GUARDIAN(ENTITY_GUARDIAN_CLASS, -0.7),
    HORSE(ENTITY_HORSE_CLASS, 0.0, "setStyle", "setAge", "setColor", "setVariant"),
    LLAMA(ENTITY_LLAMA_CLASS, 0.0, "setAge"),
    MAGMA_CUBE(ENTITY_MAGMA_CUBE_CLASS, -1.25, "setSize"),
    MUSHROOM_COW(ENTITY_MUSHROOM_COW_CLASS, -0.25, "setAge"),
    OCELOT(ENTITY_OCELOT_CLASS, -1.0, "setCatType", "setAge"),
    PARROT(ENTITY_PARROT_CLASS, -1.5, "setVariant"),
    PIG(ENTITY_PIG_CLASS, -1.0, "setAge"),
    PANDA(ENTITY_PANDA_CLASS, -0.6, "setAge", "setMainGene", "setHiddenGene"),
    RABBIT(ENTITY_RABBIT_CLASS, -1.0, "setRabbitType"),
    POLAR_BEAR(ENTITY_POLAR_BEAR_CLASS, -0.5),
    SHEEP(ENTITY_SHEEP_CLASS, -0.5, "setAge", "setSheared", "setColor"),
    SILVERFISH(ENTITY_SILVERFISH_CLASS, -1.5),
    SNOWMAN(ENTITY_SNOWMAN_CLASS, 0.0, "setHasPumpkin", "setDerp"),
    SKELETON(ENTITY_SKELETON_CLASS, 0.0),
    SHULKER(ENTITY_SHULKER_CLASS, 0.0),
    SLIME(ENTITY_SLIME_CLASS, -1.25, "setSize"),
    SPIDER(ENTITY_SPIDER_CLASS, -1.0),
    SQUID(ENTITY_SQUID_CLASS, -1.0),
    VILLAGER(ENTITY_VILLAGER_CLASS, 0.0, "setProfession", "setVillagerType", "setAge"),
    WITCH(ENTITY_WITCH_CLASS, 0.5),
    WITHER(ENTITY_WITHER_CLASS, 1.75),
    ZOMBIE(ENTITY_ZOMBIE_CLASS, 0.0, "setBaby"),
    WOLF(ENTITY_WOLF_CLASS, -1.0, "setSitting", "setTamed", "setAngry", "setAge", "setCollarColor"),
    FOX(ENTITY_FOX_CLASS, -1.0, "setFoxType", "setSitting", "setSleeping", "setAge", "setCrouching"),
    BEE(ENTITY_BEE_CLASS, -1.0, "setAnger", "setHasNectar", "setHasStung"),
    TURTLE(ENTITY_TURTLE, -1.0),
    WARDEN(ENTITY_WARDEN, 1.0),
    AXOLOTL(ENTITY_AXOLOTL_CLASS, -1.0, "setVariant", "setAge"),
    GOAT(ENTITY_GOAT_CLASS, -0.5, "setScreamingGoat", "setAge");

    private final double holoHeight;
    private final CustomizationLoader customizationLoader;
    private final Constructor<?> constructor;
    private EntityType bukkitEntityType;
    private Object nmsEntityType;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    NPCType(Class<?> entityClass, String newName, double holoHeight, String ... methods) {
        this.holoHeight = holoHeight;
        if (entityClass == null) {
            customizationLoader = null;
        } else {
            this.bukkitEntityType = EntityType.valueOf(newName.length() > 0 ? newName : this.name());
            customizationLoader = new CustomizationLoader(this.bukkitEntityType, Arrays.asList(methods));
        }
        if (entityClass == null || entityClass.isAssignableFrom(ENTITY_PLAYER_CLASS)) {
            this.constructor = null;
            return;
        }
        try {
            if (Utils.versionNewer(14)) {
                this.nmsEntityType = ((Optional<?>) ENTITY_TYPES_A_METHOD.get().invoke(null, this.bukkitEntityType.getKey().getKey().toLowerCase())).get();
                this.constructor = entityClass.getConstructor(ENTITY_TYPES_CLASS, WORLD_CLASS);
            } else {
                this.constructor = entityClass.getConstructor(WORLD_CLASS);
            }
        }
        catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    NPCType(Class<?> entityClass, double holoHeight, String ... customization) {
        this(entityClass, "", holoHeight, customization);
    }

    public double getHoloHeight() {
        return this.holoHeight;
    }

    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    public Object getNmsEntityType() {
        return this.nmsEntityType;
    }

    public CustomizationLoader getCustomizationLoader() {
        return this.customizationLoader;
    }

    public static Object[] collectArguments(String[] strings, Method method) {
        Class<?>[] methodParameterTypes = method.getParameterTypes();
        Object[] newArray = new Object[methodParameterTypes.length];
        for (int i = 0; i < methodParameterTypes.length; ++i) {
            PrimitivePropertyType primitivePropertyType = PrimitivePropertyType.forType(methodParameterTypes[i]);
            newArray[i] = primitivePropertyType != null ? primitivePropertyType.getFunction().apply(strings[i]) : EnumPropertyCache.find(strings[i], methodParameterTypes[i]);
        }
        return newArray;
    }

    public void updateCustomization(NPC npc, String name, String[] values) {
        if (!this.customizationLoader.contains(name)) {
            return;
        }
        try {
            Method method = this.customizationLoader.getMethods().get(name);
            method.invoke(npc.getBukkitEntity(), NPCType.collectArguments(values, method));
            npc.updateMetadata(npc.getViewers());
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("can't invoke method: " + name, e);
        }
    }
}