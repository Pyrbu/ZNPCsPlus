package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class NpcTypeImpl {
    private final static Map<String, NpcTypeImpl> BY_NAME = new HashMap<>();

    public static Collection<NpcTypeImpl> values() {
        return BY_NAME.values();
    }

    public static NpcTypeImpl byName(String name) {
        return BY_NAME.get(name.toUpperCase());
    }

    private final EntityType type;
    private final Set<EntityPropertyImpl<?>> allowedProperties;
    private final String name;
    private final double hologramOffset;

    private NpcTypeImpl(String name, EntityType type, double hologramOffset, Set<EntityPropertyImpl<?>> allowedProperties) {
        this.name = name.toUpperCase();
        this.type = type;
        this.hologramOffset = hologramOffset;
        this.allowedProperties = allowedProperties;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public double getHologramOffset() {
        return hologramOffset;
    }

    public Set<EntityPropertyImpl<?>> getAllowedProperties() {
        return allowedProperties;
    }

    private static NpcTypeImpl define(Builder builder) {
        return define(builder.build());
    }

    private static NpcTypeImpl define(NpcTypeImpl type) {
        BY_NAME.put(type.getName(), type);
        return type;
    }

    private static boolean defined = false;
    public static void defineTypes(PacketEventsAPI<Plugin> packetEvents) {
        if (defined) return;
        defined = true;
        ServerVersion version = packetEvents.getServerManager().getVersion();

        define(new Builder("player", EntityTypes.PLAYER).setHologramOffset(-0.15D)
                        .addProperties(EntityPropertyImpl.SKIN, EntityPropertyImpl.SKIN_LAYERS));

        define(new Builder("armor_stand", EntityTypes.ARMOR_STAND));
        define(new Builder("bat", EntityTypes.BAT).setHologramOffset(-1.365));
        define(new Builder("blaze", EntityTypes.BLAZE));
        define(new Builder("cat", EntityTypes.CAT));
        define(new Builder("cave_spider", EntityTypes.CAVE_SPIDER));
        define(new Builder("chicken", EntityTypes.CHICKEN));
        define(new Builder("cow", EntityTypes.COW));
        define(new Builder("creeper", EntityTypes.CREEPER).setHologramOffset(-0.3D));
        define(new Builder("donkey", EntityTypes.DONKEY));
        define(new Builder("elder_guardian", EntityTypes.ELDER_GUARDIAN));
        define(new Builder("ender_dragon", EntityTypes.ENDER_DRAGON));
        define(new Builder("enderman", EntityTypes.ENDERMAN));
        define(new Builder("endermite", EntityTypes.ENDERMITE));
        define(new Builder("ghast", EntityTypes.GHAST));
        define(new Builder("giant", EntityTypes.GIANT));
        define(new Builder("guardian", EntityTypes.GUARDIAN));
        define(new Builder("horse", EntityTypes.HORSE));
        define(new Builder("iron_golem", EntityTypes.IRON_GOLEM));
        define(new Builder("magma_cube", EntityTypes.MAGMA_CUBE));
        define(new Builder("mooshroom", EntityTypes.MOOSHROOM));
        define(new Builder("mule", EntityTypes.MULE));
        define(new Builder("ocelot", EntityTypes.OCELOT));
        define(new Builder("pig", EntityTypes.PIG));
        define(new Builder("rabbit", EntityTypes.RABBIT));
        define(new Builder("sheep", EntityTypes.SHEEP));
        define(new Builder("silverfish", EntityTypes.SILVERFISH));
        define(new Builder("skeleton", EntityTypes.SKELETON));
        define(new Builder("skeleton_horse", EntityTypes.SKELETON_HORSE));
        define(new Builder("slime", EntityTypes.SLIME));
        define(new Builder("snow_golem", EntityTypes.SNOW_GOLEM));
        define(new Builder("spider", EntityTypes.SPIDER));
        define(new Builder("squid", EntityTypes.SQUID));
        define(new Builder("villager", EntityTypes.VILLAGER));
        define(new Builder("witch", EntityTypes.WITCH));
        define(new Builder("wither", EntityTypes.WITHER));
        define(new Builder("wither_skeleton", EntityTypes.WITHER_SKELETON));
        define(new Builder("wolf", EntityTypes.WOLF));
        define(new Builder("zombie", EntityTypes.ZOMBIE));
        define(new Builder("zombie_horse", EntityTypes.ZOMBIE_HORSE));
        define(new Builder("zombie_villager", EntityTypes.ZOMBIE_VILLAGER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_9)) return;
        define(new Builder("shulker", EntityTypes.SHULKER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_10)) return;
        define(new Builder("husk", EntityTypes.HUSK));
        define(new Builder("polar_bear", EntityTypes.POLAR_BEAR));
        define(new Builder("stray", EntityTypes.STRAY));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_11)) return;
        define(new Builder("evoker", EntityTypes.EVOKER));
        define(new Builder("llama", EntityTypes.LLAMA));
        define(new Builder("vex", EntityTypes.VEX));
        define(new Builder("vindicator", EntityTypes.VINDICATOR));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_12)) return;
        define(new Builder("illusioner", EntityTypes.ILLUSIONER));
        define(new Builder("parrot", EntityTypes.PARROT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_13)) return;
        define(new Builder("cod", EntityTypes.COD));
        define(new Builder("dolphin", EntityTypes.DOLPHIN));
        define(new Builder("drowned", EntityTypes.DROWNED));
        define(new Builder("phantom", EntityTypes.PHANTOM));
        define(new Builder("pufferfish", EntityTypes.PUFFERFISH));
        define(new Builder("salmon", EntityTypes.SALMON));
        define(new Builder("tropical_fish", EntityTypes.TROPICAL_FISH));
        define(new Builder("turtle", EntityTypes.TURTLE));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_14)) return;
        define(new Builder("fox", EntityTypes.FOX));
        define(new Builder("panda", EntityTypes.PANDA));
        define(new Builder("pillager", EntityTypes.PILLAGER));
        define(new Builder("ravager", EntityTypes.RAVAGER));
        define(new Builder("trader_llama", EntityTypes.TRADER_LLAMA));
        define(new Builder("wandering_trader", EntityTypes.WANDERING_TRADER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_15)) return;
        define(new Builder("bee", EntityTypes.BEE));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_16)) return;
        define(new Builder("hoglin", EntityTypes.HOGLIN));
        define(new Builder("piglin", EntityTypes.PIGLIN));
        define(new Builder("piglin_brute", EntityTypes.PIGLIN_BRUTE));
        define(new Builder("strider", EntityTypes.STRIDER));
        define(new Builder("zoglin", EntityTypes.ZOGLIN));
        define(new Builder("zombified_piglin", EntityTypes.ZOMBIFIED_PIGLIN));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_17)) return;
        define(new Builder("axolotl", EntityTypes.AXOLOTL));
        define(new Builder("glow_squid", EntityTypes.GLOW_SQUID));
        define(new Builder("goat", EntityTypes.GOAT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_19)) return;
        define(new Builder("allay", EntityTypes.ALLAY));
        define(new Builder("frog", EntityTypes.FROG));
        define(new Builder("tadpole", EntityTypes.TADPOLE));
        define(new Builder("warden", EntityTypes.WARDEN));
    }

    private static final class Builder {
        private final String name;
        private final EntityType type;
        private final List<EntityPropertyImpl<?>> allowedProperties = new ArrayList<>();
        private boolean globalProperties = true;
        private double hologramOffset = 0;

        private Builder(String name, EntityType type) {
            this.name = name;
            this.type = type;
        }

        public Builder addProperties(EntityPropertyImpl<?>... properties) {
            allowedProperties.addAll(Arrays.asList(properties));
            return this;
        }

        public Builder setEnableGlobalProperties(boolean enabled) {
            globalProperties = enabled;
            return this;
        }

        public Builder setHologramOffset(double hologramOffset) {
            this.hologramOffset = hologramOffset;
            return this;
        }

        public NpcTypeImpl build() {
            if (globalProperties) {
                allowedProperties.add(EntityPropertyImpl.FIRE);
                allowedProperties.add(EntityPropertyImpl.INVISIBLE);
                allowedProperties.add(EntityPropertyImpl.SILENT);
                if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9))
                    allowedProperties.add(EntityPropertyImpl.GLOW);
            }
            return new NpcTypeImpl(name, type, hologramOffset, new HashSet<>(allowedProperties));
        }
    }
}
