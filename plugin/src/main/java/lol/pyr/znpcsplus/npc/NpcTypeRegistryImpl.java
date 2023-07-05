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

        register(builder(p, "player", EntityTypes.PLAYER)
                .setHologramOffset(-0.15D)
                .addEquipmentProperties()
                .addProperties("skin_cape", "skin_jacket", "skin_left_sleeve", "skin_right_sleeve", "skin_left_leg", "skin_right_leg", "skin_hat", "shoulder_entity_left", "shoulder_entity_right"));

        // Most hologram offsets generated using Entity#getHeight() in 1.19.4

        register(builder(p, "armor_stand", EntityTypes.ARMOR_STAND)
                .setHologramOffset(-0.15)
                .addEquipmentProperties()
                .addProperties("small", "arms", "base_plate", "head_rotation", "body_rotation", "left_arm_rotation", "right_arm_rotation", "left_leg_rotation", "right_leg_rotation"));

        register(builder(p, "bat", EntityTypes.BAT)
                .setHologramOffset(-1.075)
                .addProperties("hanging"));

        register(builder(p, "blaze", EntityTypes.BLAZE)
                .setHologramOffset(-0.175)
                .addProperties("blaze_on_fire"));

        register(builder(p, "cave_spider", EntityTypes.CAVE_SPIDER)
                .setHologramOffset(-1.475));

        register(builder(p, "chicken", EntityTypes.CHICKEN)
                .setHologramOffset(-1.275));

        register(builder(p, "cow", EntityTypes.COW)
                .setHologramOffset(-0.575));

        register(builder(p, "creeper", EntityTypes.CREEPER)
                .setHologramOffset(-0.275)
                .addProperties("creeper_state", "creeper_charged"));

        register(builder(p, "ender_dragon", EntityTypes.ENDER_DRAGON)
                .setHologramOffset(6.0245));

        register(builder(p, "enderman", EntityTypes.ENDERMAN)
                .setHologramOffset(0.925)
                .addProperties("enderman_held_block", "enderman_screaming", "enderman_staring"));

        register(builder(p, "endermite", EntityTypes.ENDERMITE)
                .setHologramOffset(-1.675));

        register(builder(p, "ghast", EntityTypes.GHAST)
                .setHologramOffset(2.025)
                .addProperties("attacking"));

        register(builder(p, "giant", EntityTypes.GIANT)
                .setHologramOffset(10.025)
                .addEquipmentProperties());

        register(builder(p, "guardian", EntityTypes.GUARDIAN)
                .setHologramOffset(-1.125));

        register(builder(p, "horse", EntityTypes.HORSE)
                .setHologramOffset(-0.375));

        register(builder(p, "iron_golem", EntityTypes.IRON_GOLEM)
                .setHologramOffset(0.725));

        register(builder(p, "magma_cube", EntityTypes.MAGMA_CUBE)); // TODO: Hologram offset scaling with size property

        register(builder(p, "mooshroom", EntityTypes.MOOSHROOM)
                .setHologramOffset(-0.575));

        register(builder(p, "ocelot", EntityTypes.OCELOT)
                .setHologramOffset(-1.275));

        register(builder(p, "pig", EntityTypes.PIG)
                .setHologramOffset(-1.075));

        register(builder(p, "rabbit", EntityTypes.RABBIT)
                .setHologramOffset(-1.475));

        register(builder(p, "sheep", EntityTypes.SHEEP)
                .setHologramOffset(-0.675));

        register(builder(p, "silverfish", EntityTypes.SILVERFISH)
                .setHologramOffset(-1.675));

        register(builder(p, "skeleton", EntityTypes.SKELETON)
                .setHologramOffset(0.015)
                .addEquipmentProperties());

        register(builder(p, "skeleton_horse", EntityTypes.SKELETON_HORSE)
                .setHologramOffset(-0.375));

        register(builder(p, "slime", EntityTypes.SLIME)); // TODO: Hologram offset scaling with size property

        register(builder(p, "snow_golem", EntityTypes.SNOW_GOLEM)
                .setHologramOffset(-0.075));

        register(builder(p, "spider", EntityTypes.SPIDER)
                .setHologramOffset(-1.075));

        register(builder(p, "squid", EntityTypes.SQUID)
                .setHologramOffset(-1.175));

        register(builder(p, "villager", EntityTypes.VILLAGER)
                .setHologramOffset(-0.025)
                .addProperties("hand", "villager_type", "villager_profession", "villager_level"));

        register(builder(p, "witch", EntityTypes.WITCH)
                .setHologramOffset(-0.025)
                .addProperties("hand"));

        register(builder(p, "wither", EntityTypes.WITHER)
                .setHologramOffset(1.525));

        register(builder(p, "wolf", EntityTypes.WOLF)
                .setHologramOffset(-1.125));

        register(builder(p, "zombie", EntityTypes.ZOMBIE)
                .setHologramOffset(-0.025)
                .addEquipmentProperties());

        register(builder(p, "zombie_horse", EntityTypes.ZOMBIE_HORSE)
                .setHologramOffset(-0.375));

        register(builder(p, "zombified_piglin", EntityTypes.ZOMBIFIED_PIGLIN)
                .setHologramOffset(-0.025)
                .addEquipmentProperties());

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_9)) return;

        register(builder(p, "shulker", EntityTypes.SHULKER)
                .setHologramOffset(-0.975));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_10)) return;

        register(builder(p, "polar_bear", EntityTypes.POLAR_BEAR)
                .setHologramOffset(-0.575));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_11)) return;

        register(builder(p, "donkey", EntityTypes.DONKEY)
                .setHologramOffset(-0.475));

        register(builder(p, "mule", EntityTypes.MULE)
                .setHologramOffset(-0.375));

        register(builder(p, "elder_guardian", EntityTypes.ELDER_GUARDIAN)
                .setHologramOffset(0.0225));

        register(builder(p, "husk", EntityTypes.HUSK)
                .setHologramOffset(-0.025)
                .addEquipmentProperties());

        register(builder(p, "polar_bear", EntityTypes.POLAR_BEAR)
                .setHologramOffset(-0.575));

        register(builder(p, "stray", EntityTypes.STRAY)
                .setHologramOffset(0.015)
                .addEquipmentProperties());

        register(builder(p, "evoker", EntityTypes.EVOKER)
                .setHologramOffset(-0.025)
                .addProperties("evoker_spell"));

        register(builder(p, "llama", EntityTypes.LLAMA)
                .setHologramOffset(-0.105));

        register(builder(p, "vex", EntityTypes.VEX)
                .setHologramOffset(-1.175)
                .addHandProperties());

        register(builder(p, "vindicator", EntityTypes.VINDICATOR)
                .setHologramOffset(-0.025));

        register(builder(p, "wither_skeleton", EntityTypes.WITHER_SKELETON)
                .setHologramOffset(0.425)
                .addEquipmentProperties());

        register(builder(p, "zombie_villager", EntityTypes.ZOMBIE_VILLAGER)
                .setHologramOffset(-1.0)
                .addEquipmentProperties());

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_12)) return;

        register(builder(p, "illusioner", EntityTypes.ILLUSIONER)
                .setHologramOffset(-0.025));

        register(builder(p, "parrot", EntityTypes.PARROT)
                .setHologramOffset(-1.075));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_13)) return;

        register(builder(p, "cod", EntityTypes.COD)
                .setHologramOffset(-1.675));

        register(builder(p, "dolphin", EntityTypes.DOLPHIN)
                .setHologramOffset(-1.375)
                .addProperties("hand"));

        register(builder(p, "drowned", EntityTypes.DROWNED)
                .setHologramOffset(-0.025)
                .addEquipmentProperties());

        register(builder(p, "phantom", EntityTypes.PHANTOM)
                .setHologramOffset(-1.475));

        register(builder(p, "pufferfish", EntityTypes.PUFFERFISH)
                .setHologramOffset(-1.625));

        register(builder(p, "salmon", EntityTypes.SALMON)
                .setHologramOffset(-1.575));

        register(builder(p, "tropical_fish", EntityTypes.TROPICAL_FISH)
                .setHologramOffset(-1.575));

        register(builder(p, "turtle", EntityTypes.TURTLE)
                .setHologramOffset(-1.575));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_14)) return;

        register(builder(p, "cat", EntityTypes.CAT)
                .setHologramOffset(-1.275)
                .addProperties("cat_variant", "cat_lying", "cat_collar_color"));

        register(builder(p, "fox", EntityTypes.FOX)
                .setHologramOffset(-1.275)
                .addProperties("hand", "fox_variant", "fox_sitting", "fox_crouching", "fox_sleeping", "fox_faceplanted"));

        register(builder(p, "panda", EntityTypes.PANDA)
                .setHologramOffset(-0.725));

        register(builder(p, "pillager", EntityTypes.PILLAGER)
                .setHologramOffset(-0.025)
                .addHandProperties());

        register(builder(p, "ravager", EntityTypes.RAVAGER)
                .setHologramOffset(0.225));

        register(builder(p, "trader_llama", EntityTypes.TRADER_LLAMA)
                .setHologramOffset(-0.105));

        register(builder(p, "wandering_trader", EntityTypes.WANDERING_TRADER)
                .setHologramOffset(-0.025)
                .addProperties("hand"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_15)) return;

        register(builder(p, "bee", EntityTypes.BEE)
                .setHologramOffset(-1.375)
                .addProperties("angry", "has_nectar"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_16)) return;

        register(builder(p, "hoglin", EntityTypes.HOGLIN)
                .setHologramOffset(-0.575));

        register(builder(p, "piglin", EntityTypes.PIGLIN)
                .setHologramOffset(-1.0)
                .addEquipmentProperties());

        register(builder(p, "piglin_brute", EntityTypes.PIGLIN_BRUTE)
                .setHologramOffset(-0.025)
                .addEquipmentProperties());

        register(builder(p, "strider", EntityTypes.STRIDER)
                .setHologramOffset(-0.275));

        register(builder(p, "zoglin", EntityTypes.ZOGLIN)
                .setHologramOffset(-0.575));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_17)) return;

        register(builder(p, "axolotl", EntityTypes.AXOLOTL)
                .setHologramOffset(-1.555)
                .addProperties("axolotl_variant", "playing_dead"));

        register(builder(p, "glow_squid", EntityTypes.GLOW_SQUID)
                .setHologramOffset(-1.175));

        register(builder(p, "goat", EntityTypes.GOAT)
                .setHologramOffset(-0.675)
                .addProperties("has_left_horn", "has_right_horn"));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_19)) return;

        register(builder(p, "allay", EntityTypes.ALLAY)
                .setHologramOffset(-1.375)
                .addHandProperties());

        register(builder(p, "frog", EntityTypes.FROG)
                .setHologramOffset(-1.475)
                .addProperties("frog_variant"));

        register(builder(p, "tadpole", EntityTypes.TADPOLE)
                .setHologramOffset(-1.675));

        register(builder(p, "warden", EntityTypes.WARDEN)
                .setHologramOffset(0.925));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_20)) return;

        register(builder(p, "sniffer", EntityTypes.SNIFFER)
                .setHologramOffset(0.125));

        register(builder(p, "camel", EntityTypes.CAMEL)
                .setHologramOffset(0.25));
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
