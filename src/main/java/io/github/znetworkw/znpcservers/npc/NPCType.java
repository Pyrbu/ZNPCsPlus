package io.github.znetworkw.znpcservers.npc;

import io.github.znetworkw.znpcservers.utility.Utils;

import static io.github.znetworkw.znpcservers.reflection.Reflections.*;

/**
 * Delete after information transferred
 */
@SuppressWarnings("unused")
public enum NPCType {
    ALLAY(ENTITY_ALLAY_CLASS, -1.14),
    ARMOR_STAND(ENTITY_ARMOR_STAND_CLASS, 0.2775, "setSmall", "setArms"),
    AXOLOTL(ENTITY_AXOLOTL_CLASS, -1.395, "setVariant", "setAge"),
    BAT(ENTITY_BAT_CLASS, -0.915, "setAwake"),
    BEE(ENTITY_BEE_CLASS, -1.215, "setAnger", "setHasNectar", "setHasStung"),
    BLAZE(ENTITY_BLAZE_CLASS, -0.015),
    CAT(ENTITY_CAT_CLASS, -1.115, "setCatType", "setAge", "setCollarColor", "setTamed"),
    CAVE_SPIDER(ENTITY_CAVE_SPIDER_CLASS, -1.315),
    CHICKEN(ENTITY_CHICKEN_CLASS, -1.115, "setAge"),
    COD(ENTITY_COD_CLASS, -1.515),
    COW(ENTITY_COW_CLASS, -0.415, "setAge"),
    CREEPER(ENTITY_CREEPER_CLASS, -0.115, "setPowered"),
    DOLPHIN(ENTITY_DOLPHIN_CLASS, -1.215),
    DONKEY(ENTITY_DONKEY_CLASS, -0.315, "setAge", "setCarryingChest"),
    DROWNED(ENTITY_DROWNED_CLASS, 0.135),
    ELDER_GUARDIAN(ENTITY_ELDER_GUARDIAN_CLASS, 0.182),
    ENDER_DRAGON(ENTITY_ENDER_DRAGON_CLASS, 6.185, "setSilent"),
    ENDERMAN(ENTITY_ENDERMAN_CLASS, 1.085),
    ENDERMITE(ENTITY_ENDERMITE_CLASS, -1.515),
    EVOKER(ENTITY_EVOKER_CLASS, 0.135, "setCurrentSpell"),
    FOX(ENTITY_FOX_CLASS, -1.115, "setFoxType", "setSitting", "setSleeping", "setAge", "setCrouching"),
    FROG(ENTITY_FROG_CLASS, -1.315, "setVariant"),
    GHAST(ENTITY_GHAST_CLASS, 2.185),
    GIANT(ENTITY_GIANT_ZOMBIE_CLASS, 10.185),
    GLOW_SQUID(ENTITY_GLOW_SQUID_CLASS, -1.015),
    GOAT(ENTITY_GOAT_CLASS, -0.515, "setScreamingGoat", "setAge", "setLeftHorn", "setRightHorn"),
    GUARDIAN(ENTITY_GUARDIAN_CLASS, -0.965),
    HORSE(ENTITY_HORSE_CLASS, -0.215, "setStyle", "setAge", "setColor", "setVariant"),
    HOGLIN(ENTITY_HOGLIN_CLASS, -0.415, "setAge"),
    HUSK(ENTITY_HUSK_CLASS, 0.135),
    ILLUSIONER(ENTITY_ILLUSIONER_CLASS, 0.135),
    IRON_GOLEM(ENTITY_IRON_GOLEM_CLASS, 0.885),
    LLAMA(ENTITY_LLAMA_CLASS, 0.055, "setAge", "setColor"),
    MAGMA_CUBE(ENTITY_MAGMA_CUBE_CLASS, 0.225, "setSize"),
    MUSHROOM_COW(ENTITY_MUSHROOM_COW_CLASS, -0.415, "setAge", "setVariant"),
    MULE(ENTITY_MULE_CLASS, -0.215, "setAge", "setCarryingChest"),
    OCELOT(ENTITY_OCELOT_CLASS, -1.115, "setCatType", "setAge"),
    PANDA(ENTITY_PANDA_CLASS, -0.565, "setAge", "setMainGene", "setHiddenGene"),
    PARROT(ENTITY_PARROT_CLASS, -0.915, "setVariant", "setAge", "setSitting"),
    PHANTOM(ENTITY_PHANTOM_CLASS, -1.315, "setSize"),
    PIG(ENTITY_PIG_CLASS, -0.915, "setAge", "setSaddle"),
    PIGLIN(ENTITY_PIGLIN_CLASS, 0.135, "setBaby"),
    PIGLIN_BRUTE(ENTITY_PIGLIN_BRUTE_CLASS, 0.135, "setBaby"),
    PILLAGER(ENTITY_PILLAGER_CLASS, 0.135),
    PLAYER(ENTITY_PLAYER_CLASS, 0.0),
    POLAR_BEAR(ENTITY_POLAR_BEAR_CLASS, -0.415, "setAge"),
    PUFFERFISH(ENTITY_PUFFERFISH_CLASS, -1.115, "setPuffState"),
    RABBIT(ENTITY_RABBIT_CLASS, -1.315, "setRabbitType"),
    RAVAGER(ENTITY_RAVAGER_CLASS, 0.385),
    SALMON(ENTITY_SALMON_CLASS, -1.415),
    SHEEP(ENTITY_SHEEP_CLASS, -0.515, "setAge", "setSheared", "setColor"),
    SHULKER(ENTITY_SHULKER_CLASS, -0.815, "setPeek", "setColor"),
    SILVERFISH(ENTITY_SILVERFISH_CLASS, -1.515),
    SKELETON(ENTITY_SKELETON_CLASS, 0.175, "setSkeletonType"),
    SKELETON_HORSE(ENTITY_SKELETON_HORSE_CLASS, -0.215),
    SLIME(ENTITY_SLIME_CLASS, 0.225, "setSize"),
    SNOWMAN(ENTITY_SNOWMAN_CLASS, 0.085, "setHasPumpkin", "setDerp"),
    SPIDER(ENTITY_SPIDER_CLASS, -0.915),
    SQUID(ENTITY_SQUID_CLASS, -1.015),
    STRAY(ENTITY_STRAY_CLASS, 0.175),
    STRIDER(ENTITY_STRIDER_CLASS, -0.115, "setSaddled", "setShivering", "setBaby"),
    TADPOLE(ENTITY_TADPOLE_CLASS, -1.515, "setAge"),
    TRADER_LLAMA(ENTITY_TRADER_LLAMA_CLASS, 0.055, "setAge", "setColor"),
    TROPICAL_FISH(ENTITY_TROPICAL_FISH_CLASS, -1.415, "setPattern", "setBodyColor", "setPatternColor"),
    TURTLE(ENTITY_TURTLE, -1.415, "setAge"),
    VEX(ENTITY_VEX_CLASS, -1.015, "setCharging"),
    VILLAGER(ENTITY_VILLAGER_CLASS, 0.135, "setProfession", "setVillagerType", "setAge", "setVillagerLevel"),
    VINDICATOR(ENTITY_VINDICATOR_CLASS, 0.135),
    WANDERING_TRADER(ENTITY_WANDERING_TRADER_CLASS, 0.135),
    WARDEN(ENTITY_WARDEN, 1.085, "setSilent"),
    WITCH(ENTITY_WITCH_CLASS, 0.135),
    WITHER(ENTITY_WITHER_CLASS, 1.685),
    WITHER_SKELETON(ENTITY_WITHER_SKELETON_CLASS, 0.585),
    WOLF(ENTITY_WOLF_CLASS, -0.965, "setSitting", "setTamed", "setAngry", "setAge", "setCollarColor"),
    ZOGLIN(ENTITY_ZOGLIN_CLASS, -0.415, "setBaby"),
    ZOMBIE(ENTITY_ZOMBIE_CLASS, 0.135, "setBaby"),
    ZOMBIE_HORSE(ENTITY_ZOMBIE_HORSE_CLASS, -0.215, "setBaby"),
    ZOMBIE_VILLAGER(ENTITY_ZOMBIE_VILLAGER_CLASS, 0.135, "setVillagerType", "setVillagerProfession", "setBaby"),
    ZOMBIFIED_PIGLIN(Utils.versionNewer(16) ? ENTITY_ZOMBIFIED_PIGLIN_CLASS : null, 0.135, "setBaby"),
    PIG_ZOMBIE(Utils.versionNewer(16) ? null : ENTITY_ZOMBIFIED_PIGLIN_CLASS, 0.135);

    NPCType(Class<?> entityClass, String newName, double holoHeight, String ... methods) {
    }

    NPCType(Class<?> entityClass, double holoHeight, String ... customization) {
    }
}