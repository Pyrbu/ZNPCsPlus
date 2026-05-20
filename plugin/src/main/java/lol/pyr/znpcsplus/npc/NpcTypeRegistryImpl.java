package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.api.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NpcTypeRegistryImpl implements NpcTypeRegistry {
    private final List<NpcTypeImpl> types = new ArrayList<>();

    private NpcTypeImpl register(NpcTypeImpl.Builder builder) {
        return register(builder.build());
    }

    private NpcTypeImpl register(NpcTypeImpl type) {
        types.add(type);
        return type;
    }

    private NpcTypeImpl.Builder builder(EntityPropertyRegistryImpl propertyRegistry, String name, EntityType type) {
        return new NpcTypeImpl.Builder(propertyRegistry, name, type);
    }

    public void registerDefault(PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl p /* propertyRegistry */) {
        ServerVersion version = packetEvents.getServerManager().getVersion();

        float DEFAULT_EYE_HEIGHT = 0.85F; // Default eye height (height * 0.85) used for hologram offset if not specified

        register(builder(p, "player", EntityTypes.PLAYER)
                .setHologramOffset(-0.15D)
                .setEyeHeight(1.62F)
                .addEquipmentProperties()
                .addProperties("skin_cape", "skin_jacket", "skin_left_sleeve", "skin_right_sleeve", "skin_left_leg", "skin_right_leg", "skin_hat", "shoulder_entity_left", "shoulder_entity_right", "force_body_rotation", "entity_sitting",
                        "tab_list_display_name", "always_visible_in_tab")
                .addDefaultProperty("skin_cape", true)
                .addDefaultProperty("skin_jacket", true)
                .addDefaultProperty("skin_left_sleeve", true)
                .addDefaultProperty("skin_right_sleeve", true)
                .addDefaultProperty("skin_left_leg", true)
                .addDefaultProperty("skin_right_leg", true)
                .addDefaultProperty("skin_hat", true));

        // Most hologram offsets generated using Entity#getHeight() in 1.19.4

        register(builder(p, "armor_stand", EntityTypes.ARMOR_STAND)
                .setHologramOffset(0)
                .setEyeHeight(1.7775F)
                .addEquipmentProperties()
                .addProperties("small", "arms", "base_plate", "head_rotation", "body_rotation", "left_arm_rotation", "right_arm_rotation", "left_leg_rotation", "right_leg_rotation"));

        register(builder(p, "bat", EntityTypes.BAT)
                .setHologramOffset(-1.075)
                .setEyeHeight(0.45F)
                .addProperties("hanging"));

        register(builder(p, "blaze", EntityTypes.BLAZE)
                .setHologramOffset(-0.175)
                .setEyeHeight(1.8F * DEFAULT_EYE_HEIGHT)
                .addProperties("blaze_on_fire"));

        register(builder(p, "cave_spider", EntityTypes.CAVE_SPIDER)
                .setHologramOffset(-1.475)
                .setEyeHeight(0.45F));

        register(builder(p, "chicken", EntityTypes.CHICKEN)
                .setHologramOffset(-1.275)
                .setEyeHeight(0.644F));

        register(builder(p, "cow", EntityTypes.COW)
                .setHologramOffset(-0.575)
                .setEyeHeight(1.3F));

        register(builder(p, "creeper", EntityTypes.CREEPER)
                .setHologramOffset(-0.275)
                .setEyeHeight(1.7F * DEFAULT_EYE_HEIGHT)
                .addProperties("creeper_state", "creeper_charged"));

        register(builder(p, "end_crystal", EntityTypes.END_CRYSTAL)
                .setHologramOffset(0.025)
                .setEyeHeight(2.0F * DEFAULT_EYE_HEIGHT)
                .addProperties("beam_target", "show_base"));

        register(builder(p, "ender_dragon", EntityTypes.ENDER_DRAGON)
                .setHologramOffset(6.0245)
                .setEyeHeight(8.0F * DEFAULT_EYE_HEIGHT)); // TODO: Scale changes if dragon is in wall (or something)

        register(builder(p, "enderman", EntityTypes.ENDERMAN)
                .setHologramOffset(0.925)
                .setEyeHeight(2.55F)
                .addProperties("enderman_held_block", "enderman_screaming", "enderman_staring", "entity_sitting"));

        register(builder(p, "endermite", EntityTypes.ENDERMITE)
                .setHologramOffset(-1.675)
                .setEyeHeight(0.13F));

        register(builder(p, "ghast", EntityTypes.GHAST)
                .setHologramOffset(2.025)
                .setEyeHeight(2.6F)
                .addProperties("attacking"));

        register(builder(p, "giant", EntityTypes.GIANT)
                .setHologramOffset(10.025)
                .setEyeHeight(10.44F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        register(builder(p, "guardian", EntityTypes.GUARDIAN)
                .setHologramOffset(-1.125)
                .setEyeHeight(0.425F)
                .addProperties("is_elder"));

        register(builder(p, "horse", EntityTypes.HORSE)
                .setHologramOffset(-0.375)
                .setEyeHeight(1.52F)
                .addProperties("horse_type", "horse_style", "horse_color", "horse_armor"));

        register(builder(p, "iron_golem", EntityTypes.IRON_GOLEM)
                .setHologramOffset(0.725)
                .setEyeHeight(1.5F));

        register(builder(p, "magma_cube", EntityTypes.MAGMA_CUBE)
                .setHologramOffset(-1.455) // TODO: Hologram offset scaling with size property
                .setEyeHeight(0.325F));

        register(builder(p, "mooshroom", EntityTypes.MOOSHROOM)
                .setHologramOffset(-0.575)
                .setEyeHeight(1.3F)
                .addProperties("mooshroom_variant"));

        register(builder(p, "ocelot", EntityTypes.OCELOT)
                .setHologramOffset(-1.275)
                .setEyeHeight(0.7F * DEFAULT_EYE_HEIGHT));

        register(builder(p, "pig", EntityTypes.PIG)
                .setHologramOffset(-1.075)
                .setEyeHeight(0.9F * DEFAULT_EYE_HEIGHT));

        register(builder(p, "rabbit", EntityTypes.RABBIT)
                .setHologramOffset(-1.475)
                .setEyeHeight(0.63F)
                .addProperties("rabbit_type"));

        register(builder(p, "sheep", EntityTypes.SHEEP)
                .setHologramOffset(-0.675)
                .setEyeHeight(1.235F)
                .addProperties("sheep_color", "sheep_sheared"));

        register(builder(p, "silverfish", EntityTypes.SILVERFISH)
                .setHologramOffset(-1.675)
                .setEyeHeight(0.13F));

        register(builder(p, "skeleton", EntityTypes.SKELETON)
                .setHologramOffset(0.015)
                .setEyeHeight(1.74F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        register(builder(p, "skeleton_horse", EntityTypes.SKELETON_HORSE)
                .setHologramOffset(-0.375)
                .setEyeHeight(1.52F));

        register(builder(p, "slime", EntityTypes.SLIME)
                .setHologramOffset(-1.455)  // TODO: Hologram offset scaling with size property
                .setEyeHeight(0.325F));

        register(builder(p, "snow_golem", EntityTypes.SNOW_GOLEM)
                .setHologramOffset(-0.075)
                .setEyeHeight(1.7F)
                .addProperties("derpy_snowgolem"));

        register(builder(p, "spider", EntityTypes.SPIDER)
                .setHologramOffset(-1.075)
                .setEyeHeight(0.65F));

        register(builder(p, "squid", EntityTypes.SQUID)
                .setHologramOffset(-1.175)
                .setEyeHeight(0.4F));

        register(builder(p, "villager", EntityTypes.VILLAGER)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.62F)
                .addProperties("hand", "villager_type", "villager_profession", "villager_level"));

        register(builder(p, "witch", EntityTypes.WITCH)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.62F)
                .addProperties("hand"));

        register(builder(p, "wither", EntityTypes.WITHER)
                .setHologramOffset(1.525)
                .setEyeHeight(3.5F * DEFAULT_EYE_HEIGHT)
                .addProperties("invulnerable_time"));

        register(builder(p, "wolf", EntityTypes.WOLF)
                .setHologramOffset(-1.125)
                .setEyeHeight(0.68F)
                .addProperties("wolf_begging", "wolf_collar", "wolf_angry"));

        register(builder(p, "zombie", EntityTypes.ZOMBIE)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.74F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        register(builder(p, "zombie_horse", EntityTypes.ZOMBIE_HORSE)
                .setHologramOffset(-0.375)
                .setEyeHeight(1.52F));

        register(builder(p, "zombified_piglin", EntityTypes.ZOMBIFIED_PIGLIN)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.79F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_9)) return;

        register(builder(p, "shulker", EntityTypes.SHULKER)
                .setHologramOffset(-0.975)
                .setEyeHeight(0.5F)
                .addProperties("attach_direction", "shield_height", "shulker_color"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_10)) return;

        register(builder(p, "polar_bear", EntityTypes.POLAR_BEAR)
                .setHologramOffset(-0.575)
                .setEyeHeight(1.4F * DEFAULT_EYE_HEIGHT) // TODO: Eye height change on stand animation (but its client side)
                .addProperties("polar_bear_standing"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_11)) return;

        register(builder(p, "donkey", EntityTypes.DONKEY)
                .setHologramOffset(-0.475)
                .setEyeHeight(1.425F));

        register(builder(p, "mule", EntityTypes.MULE)
                .setHologramOffset(-0.375)
                .setEyeHeight(1.52F));

        register(builder(p, "elder_guardian", EntityTypes.ELDER_GUARDIAN)
                .setHologramOffset(0.0225)
                .setEyeHeight(0.99875F));

        register(builder(p, "husk", EntityTypes.HUSK)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.74F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        register(builder(p, "stray", EntityTypes.STRAY)
                .setHologramOffset(0.015)
                .setEyeHeight(1.74F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        register(builder(p, "evoker", EntityTypes.EVOKER)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.95F * DEFAULT_EYE_HEIGHT)
                .addProperties("entity_sitting"));

        register(builder(p, "llama", EntityTypes.LLAMA)
                .setHologramOffset(-0.105)
                .setEyeHeight(1.7765F)
                .addProperties("carpet_color", "llama_variant", "body"));

        register(builder(p, "vex", EntityTypes.VEX)
                .setHologramOffset(-1.175)
                .setEyeHeight(0.51875F)
                .addHandProperties());

        register(builder(p, "vindicator", EntityTypes.VINDICATOR)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.95F * DEFAULT_EYE_HEIGHT)
                .addProperties("celebrating", "entity_sitting"));

        register(builder(p, "wither_skeleton", EntityTypes.WITHER_SKELETON)
                .setHologramOffset(0.425)
                .setEyeHeight(2.1F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        register(builder(p, "zombie_villager", EntityTypes.ZOMBIE_VILLAGER)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.74F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_12)) return;

        register(builder(p, "illusioner", EntityTypes.ILLUSIONER)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.95F * DEFAULT_EYE_HEIGHT) // TODO: Scale change with tick (maybe client side only?)
                .addProperties("entity_sitting"));

        register(builder(p, "parrot", EntityTypes.PARROT)
                .setHologramOffset(-1.075)
                .setEyeHeight(0.54F)
                .addProperties("parrot_variant"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_13)) return;

        register(builder(p, "cod", EntityTypes.COD)
                .setHologramOffset(-1.675)
                .setEyeHeight(0.195F));

        register(builder(p, "dolphin", EntityTypes.DOLPHIN)
                .setHologramOffset(-1.375)
                .setEyeHeight(0.3F)
                .addProperties("hand"));

        register(builder(p, "drowned", EntityTypes.DROWNED)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.74F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        register(builder(p, "phantom", EntityTypes.PHANTOM)
                .setHologramOffset(-1.475)
                .setEyeHeight(0.175F));

        register(builder(p, "pufferfish", EntityTypes.PUFFERFISH)
                .setHologramOffset(-1.625)
                .setEyeHeight(0.455F)
                .addProperties("puff_state"));

        register(builder(p, "salmon", EntityTypes.SALMON)
                .setHologramOffset(-1.575)
                .setEyeHeight(0.26F));

        register(builder(p, "tropical_fish", EntityTypes.TROPICAL_FISH)
                .setHologramOffset(-1.575)
                .setEyeHeight(0.26F)
                .addProperties("tropical_fish_pattern", "tropical_fish_body_color", "tropical_fish_pattern_color"));

        register(builder(p, "turtle", EntityTypes.TURTLE)
                .setHologramOffset(-1.575)
                .setEyeHeight(0.4F * DEFAULT_EYE_HEIGHT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_14)) return;

        register(builder(p, "cat", EntityTypes.CAT)
                .setHologramOffset(-1.275)
                .setEyeHeight(0.35F)
                .addProperties("cat_variant", "cat_laying", "cat_relaxed", "cat_collar"));

        register(builder(p, "fox", EntityTypes.FOX)
                .setHologramOffset(-1.275)
                .setEyeHeight(0.4F)
                .addProperties("hand", "fox_variant", "fox_sitting", "fox_crouching", "fox_sleeping", "fox_faceplanted"));

        register(builder(p, "panda", EntityTypes.PANDA)
                .setHologramOffset(-0.725)
                .setEyeHeight(1.25F * DEFAULT_EYE_HEIGHT)
                .addProperties("panda_main_gene", "panda_hidden_gene", "panda_sneezing"));

        register(builder(p, "pillager", EntityTypes.PILLAGER)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.95F * DEFAULT_EYE_HEIGHT)
                .addHandProperties()
                .addProperties("pillager_charging", "entity_sitting"));

        register(builder(p, "ravager", EntityTypes.RAVAGER)
                .setHologramOffset(0.225)
                .setEyeHeight(2.2F * DEFAULT_EYE_HEIGHT));

        register(builder(p, "trader_llama", EntityTypes.TRADER_LLAMA)
                .setHologramOffset(-0.105)
                .setEyeHeight(1.7765F)
                .addProperties("carpet_color", "llama_variant", "body"));

        register(builder(p, "wandering_trader", EntityTypes.WANDERING_TRADER)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.62F)
                .addProperties("hand"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_15)) return;

        register(builder(p, "bee", EntityTypes.BEE)
                .setHologramOffset(-1.375)
                .setEyeHeight(0.3F)
                .addProperties("angry", "has_nectar"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_16)) return;

        register(builder(p, "hoglin", EntityTypes.HOGLIN)
                .setHologramOffset(-0.575)
                .setEyeHeight(1.4F * DEFAULT_EYE_HEIGHT)
                .addProperties("hoglin_immune_to_zombification"));

        register(builder(p, "piglin", EntityTypes.PIGLIN)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.79F)
                .addEquipmentProperties()
                .addProperties("piglin_baby", "piglin_charging_crossbow", "piglin_dancing", "entity_sitting"));

        register(builder(p, "piglin_brute", EntityTypes.PIGLIN_BRUTE)
                .setHologramOffset(-0.025)
                .setEyeHeight(1.79F)
                .addEquipmentProperties()
                .addProperties("entity_sitting"));

        register(builder(p, "strider", EntityTypes.STRIDER)
                .setHologramOffset(-0.275)
                .setEyeHeight(1.7F * DEFAULT_EYE_HEIGHT)
                .addProperties("strider_shaking"));

        register(builder(p, "zoglin", EntityTypes.ZOGLIN)
                .setHologramOffset(-0.575)
                .setEyeHeight(1.4F * DEFAULT_EYE_HEIGHT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_17)) return;

        register(builder(p, "axolotl", EntityTypes.AXOLOTL)
                .setHologramOffset(-1.555)
                .setEyeHeight(0.2751F)
                .addProperties("axolotl_variant", "playing_dead"));

        register(builder(p, "glow_squid", EntityTypes.GLOW_SQUID)
                .setHologramOffset(-1.175)
                .setEyeHeight(0.4F));

        register(builder(p, "goat", EntityTypes.GOAT)
                .setHologramOffset(-0.675)
                .setEyeHeight(1.3F * DEFAULT_EYE_HEIGHT) // TODO: Long jumping has different scale
                .addProperties("has_left_horn", "has_right_horn"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_19)) return;

        register(builder(p, "allay", EntityTypes.ALLAY)
                .setHologramOffset(-1.375)
                .setEyeHeight(0.36F)
                .addHandProperties());

        register(builder(p, "frog", EntityTypes.FROG)
                .setHologramOffset(-1.475)
                .setEyeHeight(0.5F * DEFAULT_EYE_HEIGHT)
                .addProperties("frog_variant", "frog_target_npc"));

        register(builder(p, "tadpole", EntityTypes.TADPOLE)
                .setHologramOffset(-1.675)
                .setEyeHeight(0.19500001F));

        register(builder(p, "warden", EntityTypes.WARDEN)
                .setHologramOffset(0.925)
                .setEyeHeight(2.9F * DEFAULT_EYE_HEIGHT) // TODO: height changes when its digging or emerging
                .addProperties("warden_anger"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_20)) return;

        register(builder(p, "sniffer", EntityTypes.SNIFFER)
                .setHologramOffset(0.075)
                .setEyeHeight(1.05F)
                .addProperties("sniffer_state"));

        register(builder(p, "camel", EntityTypes.CAMEL)
                .setHologramOffset(0.4)
                .setEyeHeight(2.275F)
                .addProperties("bashing", "camel_sitting"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_20_5)) return;

        register(builder(p, "armadillo", EntityTypes.ARMADILLO)
                .setHologramOffset(-1.325)
                .setEyeHeight(0.26F)
                .addProperties("armadillo_state"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_21)) return;

        register(builder(p, "bogged", EntityTypes.BOGGED)
                .setHologramOffset(0.015)
                .setEyeHeight(1.74F)
                .addProperties("bogged_sheared", "entity_sitting"));

        register(builder(p, "breeze", EntityTypes.BREEZE)
                .setHologramOffset(-0.205)
                .setEyeHeight(1.3452F));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_21_2)) return;

        register(builder(p, "creaking", EntityTypes.CREAKING)
                .setHologramOffset(0.725)
                .setEyeHeight(2.3F)
                .addProperties("creaking_active"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) return;

        register(builder(p, "happy_ghast", EntityTypes.HAPPY_GHAST)
                .setHologramOffset(2)
                .setEyeHeight(2.6F));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_21_9)) return;

        register(builder(p, "copper_golem", EntityTypes.COPPER_GOLEM)
                .setHologramOffset(-0.995)
                .setEyeHeight(0.8125F)
                .addProperties("weathering_copper_state", "copper_golem_state"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_21_11)) return;

        register(builder(p, "nautilus", EntityTypes.NAUTILUS)
                .setHologramOffset(-1.025)
                .setEyeHeight(0.2751F));

        register(builder(p, "zombie_nautilus", EntityTypes.ZOMBIE_NAUTILUS)
                .setHologramOffset(-1.025)
                .setEyeHeight(0.2751F)
                .addProperties("zombie_nautilus_variant"));

        register(builder(p, "camel_husk", EntityTypes.CAMEL_HUSK)
                .setHologramOffset(0.4)
                .setEyeHeight(2.275F)
                .addProperties("bashing", "camel_sitting"));

        register(builder(p, "parched", EntityTypes.PARCHED)
                .setHologramOffset(0.015)
                .setEyeHeight(1.74F)
                .addProperties("entity_sitting"));
    }

    public Collection<NpcType> getAll() {
        return Collections.unmodifiableList(types);
    }

    public Collection<NpcTypeImpl> getAllImpl() {
        return Collections.unmodifiableList(types);
    }

    public NpcTypeImpl getByName(String name) {
        for (NpcTypeImpl type : types) if (type.getName().equalsIgnoreCase(name)) return type;
        return null;
    }

    public NpcTypeImpl getByEntityType(EntityType entityType) {
        for (NpcTypeImpl type : types) if (type.getType() == entityType) return type;
        return null;
    }
}
