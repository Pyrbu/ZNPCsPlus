package io.github.znetworkw.znpcservers.cache;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.utility.Utils;
import io.netty.channel.Channel;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class CacheRegistry {
    public static final Class<?> PACKET_PLAY_IN_USE_ENTITY_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayInUseEntity"))).load();

    public static final Class<?> ENUM_PLAYER_INFO_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutPlayerInfo$EnumPlayerInfoAction")
            .withClassName("ClientboundPlayerInfoUpdatePacket$a"))).load();

    public static final Class<?> PACKET_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PROTOCOL)
            .withClassName("Packet"))).load();

    public static final Class<?> ENTITY_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withClassName("Entity"))).load();

    public static final Class<?> ENTITY_LIVING = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withClassName("EntityLiving"))).load();

    public static final Class<?> ENTITY_PLAYER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SERVER_LEVEL)
            .withClassName("EntityPlayer"))).load();

    public static final Class<?> ENTITY_ARMOR_STAND_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("decoration")
            .withClassName("EntityArmorStand"))).load();

    public static final Class<?> ENTITY_BAT_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("ambient")
            .withClassName("EntityBat"))).load();

    public static final Class<?> ENTITY_BLAZE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityBlaze"))).load();

    public static final Class<?> ENTITY_CAVE_SPIDER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityCaveSpider"))).load();

    public static final Class<?> ENTITY_CHICKEN_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityChicken"))).load();

    public static final Class<?> ENTITY_COW_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityCow"))).load();

    public static final Class<?> ENTITY_CREEPER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityCreeper"))).load();

    public static final Class<?> ENTITY_ENDER_DRAGON_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("boss.enderdragon")
            .withClassName("EntityEnderDragon"))).load();

    public static final Class<?> ENTITY_ENDERMAN_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityEnderman"))).load();

    public static final Class<?> ENTITY_HUMAN_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("player")
            .withClassName("EntityHuman"))).load();

    public static final Class<?> ENTITY_ENDERMITE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityEndermite"))).load();

    public static final Class<?> ENTITY_GHAST_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityGhast"))).load();

    public static final Class<?> ENTITY_IRON_GOLEM_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityIronGolem"))).load();

    public static final Class<?> ENTITY_GIANT_ZOMBIE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityGiantZombie"))).load();

    public static final Class<?> ENTITY_GUARDIAN_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityGuardian"))).load();

    public static final Class<?> ENTITY_HORSE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal.horse")
            .withClassName("EntityHorse"))).load();

    public static final Class<?> ENTITY_LLAMA_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal.horse")
            .withClassName("EntityLlama"))).load();

    public static final Class<?> ENTITY_MAGMA_CUBE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityMagmaCube"))).load();

    public static final Class<?> ENTITY_MUSHROOM_COW_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityMushroomCow"))).load();

    public static final Class<?> ENTITY_OCELOT_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityOcelot"))).load();

    public static final Class<?> ENTITY_TURTLE = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityTurtle"))).load();

    public static final Class<?> ENTITY_WARDEN = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster.warden")
            .withClassName("Warden"))).load();

    public static final Class<?> ENTITY_BEE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityBee"))).load();

    public static final Class<?> ENTITY_PARROT_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityParrot"))).load();

    public static final Class<?> ENTITY_PIG_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityPig"))).load();

    public static final Class<?> ENTITY_RABBIT_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityRabbit"))).load();

    public static final Class<?> ENTITY_POLAR_BEAR_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityPolarBear"))).load();

    public static final Class<?> ENTITY_PANDA_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityPanda"))).load();

    public static final Class<?> ENTITY_SHEEP_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntitySheep"))).load();

    public static final Class<?> ENTITY_SNOWMAN_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntitySnowman"))).load();

    public static final Class<?> ENTITY_SHULKER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityShulker"))).load();

    public static final Class<?> ENTITY_SILVERFISH_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntitySilverfish"))).load();

    public static final Class<?> ENTITY_SKELETON_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntitySkeleton"))).load();

    public static final Class<?> ENTITY_SLIME_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntitySlime"))).load();

    public static final Class<?> ENTITY_SPIDER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntitySpider"))).load();

    public static final Class<?> ENTITY_SQUID_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntitySquid"))).load();

    public static final Class<?> ENTITY_VILLAGER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("npc")
            .withClassName("EntityVillager"))).load();

    public static final Class<?> ENTITY_WITCH_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityWitch"))).load();

    public static final Class<?> ENTITY_WITHER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("boss.wither")
            .withClassName("EntityWither"))).load();

    public static final Class<?> ENTITY_ZOMBIE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("monster")
            .withClassName("EntityZombie"))).load();

    public static final Class<?> ENTITY_WOLF_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityWolf"))).load();

    public static final Class<?> ENTITY_AXOLOTL_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal.axolotl")
            .withClassName("Axolotl"))).load();

    public static final Class<?> ENTITY_GOAT_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal.goat")
            .withClassName("Goat"))).load();

    public static final Class<?> ENTITY_FOX_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withAdditionalData("animal")
            .withClassName("EntityFox"))).load();

    public static final Class<?> ENTITY_TYPES_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withClassName("EntityTypes"))).load();

    public static final Class<?> ENUM_CHAT_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withClassName("EnumChatFormat"))).load();

    public static final Class<?> ENUM_ITEM_SLOT = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withClassName("EnumItemSlot"))).load();

    public static final Class<?> I_CHAT_BASE_COMPONENT = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.CHAT)
            .withClassName("IChatBaseComponent"))).load();

    public static final Class<?> ITEM_STACK_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ITEM)
            .withClassName("ItemStack"))).load();

    public static final Class<?> DATA_WATCHER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SYNCHER)
            .withClassName("DataWatcher"))).load();

    public static final Class<?> DATA_WATCHER_OBJECT = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SYNCHER)
            .withClassName("DataWatcherObject"))).load();

    public static final Class<?> DATA_WATCHER_REGISTRY = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SYNCHER)
            .withClassName("DataWatcherRegistry"))).load();

    public static final Class<?> DATA_WATCHER_SERIALIZER = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SYNCHER)
            .withClassName("DataWatcherSerializer"))).load();

    public static final Class<?> WORLD_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.WORLD_LEVEL)
            .withClassName("World"))).load();

    public static final Class<?> CRAFT_ITEM_STACK_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.CRAFT_BUKKIT))

            .withClassName("inventory.CraftItemStack"))).load();

    public static final Class<?> WORLD_SERVER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SERVER_LEVEL)
            .withClassName("WorldServer"))).load();

    public static final Class<?> MINECRAFT_SERVER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SERVER)
            .withClassName("MinecraftServer"))).load();

    public static final Class<?> PLAYER_INTERACT_MANAGER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SERVER_LEVEL)
            .withClassName("PlayerInteractManager"))).load();

    public static final Class<?> PLAYER_CONNECTION_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SERVER_NETWORK)
            .withClassName("PlayerConnection"))).load();

    public static final Class<?> NETWORK_MANAGER_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.NETWORK)
            .withClassName("NetworkManager"))).load();

    public static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutPlayerInfo")
            .withClassName("ClientboundPlayerInfoUpdatePacket")))
            .load();

    public static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO_REMOVE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("ClientboundPlayerInfoRemovePacket")))
            .load();

    public static final Class<?> PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutScoreboardTeam"))).load();

    public static final Class<?> PACKET_PLAY_OUT_ENTITY_DESTROY_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntityDestroy"))).load();

    public static final Class<?> SCOREBOARD_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.WORLD_SCORES)
            .withClassName("Scoreboard"))).load();

    public static final Class<?> SCOREBOARD_TEAM_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.WORLD_SCORES)
            .withClassName("ScoreboardTeam"))).load();

    public static final Class<?> ENUM_TAG_VISIBILITY = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.WORLD_SCORES)
            .withClassName("ScoreboardTeamBase$EnumNameTagVisibility"))).load();

    public static final Class<?> CRAFT_CHAT_MESSAGE_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.CRAFT_BUKKIT))

            .withClassName("util.CraftChatMessage"))).load();

    public static final Class<?> PROFILE_PUBLIC_KEY_CLASS = (new TypeCache.BaseCache.ClazzLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.WORLD_ENTITY_PLAYER)
            .withClassName("ProfilePublicKey"))).load();

    public static final TypeCache.BaseCache<Constructor<?>> SCOREBOARD_TEAM_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(SCOREBOARD_TEAM_CLASS)
            .withParameterTypes(SCOREBOARD_CLASS, String.class));

    public static final TypeCache.BaseCache<Constructor<?>> PLAYER_CONSTRUCTOR_OLD = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_PLAYER_CLASS)
            .withParameterTypes(MINECRAFT_SERVER_CLASS, WORLD_SERVER_CLASS, GameProfile.class, PLAYER_INTERACT_MANAGER_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PLAYER_CONSTRUCTOR_NEW = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_PLAYER_CLASS)
            .withParameterTypes(MINECRAFT_SERVER_CLASS, WORLD_SERVER_CLASS, GameProfile.class));

    public static final TypeCache.BaseCache<Constructor<?>> PLAYER_CONSTRUCTOR_NEW_1 = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_PLAYER_CLASS)
            .withParameterTypes(MINECRAFT_SERVER_CLASS, WORLD_SERVER_CLASS, GameProfile.class, PROFILE_PUBLIC_KEY_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PLAYER_CONSTRUCTOR_NEW_2 = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_PLAYER_CLASS)
            .withParameterTypes(MINECRAFT_SERVER_CLASS, WORLD_SERVER_CLASS, GameProfile.class));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(PACKET_PLAY_OUT_PLAYER_INFO_CLASS)
            .withParameterTypes(ENUM_PLAYER_INFO_CLASS, (Utils.BUKKIT_VERSION > 16) ? Collection.class : Iterable.class).withParameterTypes(ENUM_PLAYER_INFO_CLASS, ENTITY_PLAYER_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_PLAYER_INFO_REMOVE_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(PACKET_PLAY_OUT_PLAYER_INFO_REMOVE_CLASS)
            .withParameterTypes(List.class));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_LOOK_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntity$PacketPlayOutEntityLook")
            .withParameterTypes(int.class, byte.class, byte.class, boolean.class));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntityHeadRotation")
            .withParameterTypes(ENTITY_CLASS, byte.class));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntityTeleport")
            .withParameterTypes(ENTITY_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntityMetadata")
            .withParameterTypes(int.class, DATA_WATCHER_CLASS, boolean.class));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR_V1 = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntityMetadata")
            .withParameterTypes(int.class, List.class));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_NAMED_ENTITY_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutNamedEntitySpawn")
            .withParameterTypes(ENTITY_HUMAN_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(PACKET_PLAY_OUT_ENTITY_DESTROY_CLASS)
            .withParameterTypes(int.class).withParameterTypes(int[].class));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutSpawnEntity")
            .withClassName("PacketPlayOutSpawnEntityLiving")
            .withParameterTypes(ENTITY_LIVING).withParameterTypes(ENTITY_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PLAYER_INTERACT_MANAGER_OLD_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PlayerInteractManager")
            .withParameterTypes(WORLD_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PLAYER_INTERACT_MANAGER_NEW_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PlayerInteractManager")
            .withParameterTypes(WORLD_SERVER_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_OLD = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntityEquipment")
            .withParameterTypes(int.class, int.class, ITEM_STACK_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_NEWEST_OLD = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntityEquipment")
            .withParameterTypes(int.class, ENUM_ITEM_SLOT, ITEM_STACK_CLASS));

    public static final TypeCache.BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_V1 = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutEntityEquipment")
            .withParameterTypes(int.class, List.class));

    public static final TypeCache.BaseCache<Constructor<?>> I_CHAT_BASE_COMPONENT_A_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.CHAT)
            .withClassName("ChatComponentText")
            .withParameterTypes(String.class));

    public static final TypeCache.BaseCache<Constructor<?>> ENTITY_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_ARMOR_STAND_CLASS)
            .withParameterTypes(WORLD_CLASS, double.class, double.class, double.class));

    public static final TypeCache.BaseCache<Constructor<?>> DATA_WATCHER_OBJECT_CONSTRUCTOR = new TypeCache.BaseCache.ConstructorLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(DATA_WATCHER_OBJECT)
            .withParameterTypes(int.class, DATA_WATCHER_SERIALIZER));

    public static final TypeCache.BaseCache<Method> AS_NMS_COPY_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.CRAFT_BUKKIT))

            .withClassName("inventory.CraftItemStack")
            .withMethodName("asNMSCopy")
            .withParameterTypes(ItemStack.class));

    public static final TypeCache.BaseCache<Method> GET_PROFILE_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withClassName(ENTITY_HUMAN_CLASS)
            .withExpectResult(GameProfile.class));

    public static final TypeCache.BaseCache<Method> GET_ENTITY_ID = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_CLASS)
            .withMethodName("getId")
            .withMethodName("ae")
            .withMethodName("ah")
            .withExpectResult(int.class));

    public static final TypeCache.BaseCache<Method> GET_HANDLE_PLAYER_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.CRAFT_BUKKIT))

            .withClassName("entity.CraftPlayer").withClassName("entity.CraftHumanEntity")
            .withMethodName("getHandle"));

    public static final TypeCache.BaseCache<Method> GET_HANDLE_WORLD_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.CRAFT_BUKKIT))

            .withClassName("CraftWorld")
            .withMethodName("getHandle"));

    public static final TypeCache.BaseCache<Method> GET_SERVER_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.CRAFT_BUKKIT))

            .withClassName("CraftServer")
            .withMethodName("getServer"));

    public static final TypeCache.BaseCache<Method> SEND_PACKET_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(PLAYER_CONNECTION_CLASS)
            .withMethodName("sendPacket").withMethodName("a")
            .withParameterTypes(PACKET_CLASS));

    public static final TypeCache.BaseCache<Method> SET_CUSTOM_NAME_OLD_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_CLASS)
            .withMethodName("setCustomName")
            .withParameterTypes(String.class));

    public static final TypeCache.BaseCache<Method> SET_CUSTOM_NAME_NEW_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_CLASS)
            .withMethodName("setCustomName")
            .withMethodName("a")
            .withMethodName("b")
            .withParameterTypes(I_CHAT_BASE_COMPONENT).withExpectResult(void.class));

    public static final TypeCache.BaseCache<Method> SET_CUSTOM_NAME_VISIBLE_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_CLASS)
            .withMethodName("setCustomNameVisible")
            .withMethodName("n")
            .withParameterTypes(boolean.class));

    public static final TypeCache.BaseCache<Method> SET_INVISIBLE_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_ARMOR_STAND_CLASS)
            .withMethodName("setInvisible").withMethodName("j")
            .withParameterTypes(boolean.class));

    public static final TypeCache.BaseCache<Method> SET_LOCATION_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_CLASS)
            .withMethodName("setPositionRotation")
            .withMethodName("a")
            .withParameterTypes(double.class, double.class, double.class, float.class, float.class));

    public static final TypeCache.BaseCache<Method> SET_DATA_WATCHER_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(DATA_WATCHER_CLASS)
            .withMethodName("set").withMethodName("b")
            .withParameterTypes(DATA_WATCHER_OBJECT, Object.class));

    public static final TypeCache.BaseCache<Method> WATCH_DATA_WATCHER_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(DATA_WATCHER_CLASS)
            .withMethodName("watch")
            .withParameterTypes(int.class, Object.class));

    public static final TypeCache.BaseCache<Method> GET_DATA_WATCHER_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_CLASS)
            .withMethodName("getDataWatcher")
            .withMethodName("ai")
            .withMethodName("al")
            .withExpectResult(DATA_WATCHER_CLASS));

    public static final TypeCache.BaseCache<Method> GET_BUKKIT_ENTITY_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENTITY_CLASS)
            .withMethodName("getBukkitEntity"));

    public static final TypeCache.BaseCache<Method> GET_ENUM_CHAT_ID_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENUM_CHAT_CLASS)
            .withMethodName("b"));

    public static final TypeCache.BaseCache<Method> ENUM_CHAT_TO_STRING_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENUM_CHAT_CLASS)
            .withExpectResult(String.class)
            .withMethodName("toString"));

    public static final TypeCache.BaseCache<Method> ENTITY_TYPES_A_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.ENTITY)
            .withClassName(ENTITY_TYPES_CLASS)
            .withMethodName("a")
            .withParameterTypes(String.class));

    public static final TypeCache.BaseCache<Method> PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE_V1 = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS)
            .withMethodName("a")
            .withParameterTypes(SCOREBOARD_TEAM_CLASS));

    public static final TypeCache.BaseCache<Method> PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS)
            .withMethodName("a")
            .withParameterTypes(SCOREBOARD_TEAM_CLASS, boolean.class));

    public static final TypeCache.BaseCache<Method> SCOREBOARD_PLAYER_LIST = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(SCOREBOARD_TEAM_CLASS)
            .withMethodName("getPlayerNameSet").withMethodName("g"));

    public static final TypeCache.BaseCache<Method> ENUM_CHAT_FORMAT_FIND = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withClassName(ENUM_CHAT_CLASS)
            .withParameterTypes(String.class).withExpectResult(ENUM_CHAT_CLASS));

    public static final TypeCache.BaseCache<Method> CRAFT_CHAT_MESSAGE_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.CRAFT_BUKKIT))

            .withClassName(CRAFT_CHAT_MESSAGE_CLASS)
            .withMethodName("fromStringOrNull")
            .withParameterTypes(String.class));

    public static final TypeCache.BaseCache<Method> GET_UNIQUE_ID_METHOD = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withClassName(ENTITY_CLASS)
            .withExpectResult(UUID.class));

    public static final TypeCache.BaseCache<Method> GET_DATAWATCHER_B_LIST = new TypeCache.BaseCache.MethodLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withMethodName("c")
            .withClassName(DATA_WATCHER_CLASS));

    public static final TypeCache.BaseCache<Field> PLAYER_CONNECTION_FIELD = new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SERVER_LEVEL)
            .withClassName(ENTITY_PLAYER_CLASS)
            .withFieldName((Utils.BUKKIT_VERSION > 16) ? "b" : "playerConnection"));

    public static final TypeCache.BaseCache<Field> NETWORK_MANAGER_FIELD = new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(PLAYER_CONNECTION_CLASS)
            .withFieldName((Utils.BUKKIT_VERSION > 16) ? "a" : "networkManager")
            .withExpectResult(NETWORK_MANAGER_CLASS));

    public static final TypeCache.BaseCache<Field> CHANNEL_FIELD = new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.SERVER_NETWORK)
            .withClassName(NETWORK_MANAGER_CLASS)
            .withExpectResult(Channel.class));

    public static final TypeCache.BaseCache<Field> PACKET_IN_USE_ENTITY_ID_FIELD = new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayInUseEntity")
            .withFieldName("a"));

    public static final TypeCache.BaseCache<Field> BUKKIT_COMMAND_MAP = new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.CRAFT_BUKKIT))

            .withClassName("CraftServer")
            .withFieldName("commandMap"));

    public static final TypeCache.BaseCache<Object> ADD_PLAYER_FIELD = (new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutPlayerInfo$EnumPlayerInfoAction")
            .withClassName("ClientboundPlayerInfoUpdatePacket$a")
            .withFieldName((Utils.BUKKIT_VERSION > 16) ? "a" : "ADD_PLAYER"))).asValueField();

    public static final TypeCache.BaseCache<Object> UPDATE_LISTED_FIELD = (new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("ClientboundPlayerInfoUpdatePacket$a")
            .withFieldName("d"))).asValueField();

    public static final TypeCache.BaseCache<Object> REMOVE_PLAYER_FIELD = (new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName("PacketPlayOutPlayerInfo$EnumPlayerInfoAction")
            .withClassName("ClientboundPlayerInfoUpdatePacket$a")
            .withFieldName((Utils.BUKKIT_VERSION > 16) ? "e" : "REMOVE_PLAYER"))).asValueField();

    public static final TypeCache.BaseCache<Object> DATA_WATCHER_REGISTER_FIELD = (new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(DATA_WATCHER_REGISTRY)
            .withFieldName("a"))).asValueField();

    public static final TypeCache.BaseCache<Object> ENUM_TAG_VISIBILITY_NEVER_FIELD = (new TypeCache.BaseCache.FieldLoader((new TypeCache.CacheBuilder(CachePackage.MINECRAFT_SERVER))

            .withCategory(CacheCategory.PACKET)
            .withClassName(ENUM_TAG_VISIBILITY)
            .withFieldName("b"))).asValueField();
}
