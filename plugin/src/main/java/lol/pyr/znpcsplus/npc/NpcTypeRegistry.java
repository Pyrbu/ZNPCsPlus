package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NpcTypeRegistry {
    private final List<NpcTypeImpl> types = new ArrayList<>();

    private NpcTypeImpl define(NpcTypeImpl.Builder builder) {
        return define(builder.build());
    }

    private NpcTypeImpl define(NpcTypeImpl type) {
        types.add(type);
        return type;
    }

    public void registerDefault(PacketEventsAPI<Plugin> packetEvents) {
        ServerVersion version = packetEvents.getServerManager().getVersion();

        define(new NpcTypeImpl.Builder("player", EntityTypes.PLAYER).setHologramOffset(-0.15D)
                .addProperties(EntityPropertyImpl.SKIN, EntityPropertyImpl.SKIN_LAYERS));

        define(new NpcTypeImpl.Builder("armor_stand", EntityTypes.ARMOR_STAND));
        define(new NpcTypeImpl.Builder("bat", EntityTypes.BAT).setHologramOffset(-1.365));
        define(new NpcTypeImpl.Builder("blaze", EntityTypes.BLAZE));
        define(new NpcTypeImpl.Builder("cat", EntityTypes.CAT));
        define(new NpcTypeImpl.Builder("cave_spider", EntityTypes.CAVE_SPIDER));
        define(new NpcTypeImpl.Builder("chicken", EntityTypes.CHICKEN));
        define(new NpcTypeImpl.Builder("cow", EntityTypes.COW));
        define(new NpcTypeImpl.Builder("creeper", EntityTypes.CREEPER).setHologramOffset(-0.3D));
        define(new NpcTypeImpl.Builder("donkey", EntityTypes.DONKEY));
        define(new NpcTypeImpl.Builder("elder_guardian", EntityTypes.ELDER_GUARDIAN));
        define(new NpcTypeImpl.Builder("ender_dragon", EntityTypes.ENDER_DRAGON));
        define(new NpcTypeImpl.Builder("enderman", EntityTypes.ENDERMAN));
        define(new NpcTypeImpl.Builder("endermite", EntityTypes.ENDERMITE));
        define(new NpcTypeImpl.Builder("ghast", EntityTypes.GHAST));
        define(new NpcTypeImpl.Builder("giant", EntityTypes.GIANT));
        define(new NpcTypeImpl.Builder("guardian", EntityTypes.GUARDIAN));
        define(new NpcTypeImpl.Builder("horse", EntityTypes.HORSE));
        define(new NpcTypeImpl.Builder("iron_golem", EntityTypes.IRON_GOLEM));
        define(new NpcTypeImpl.Builder("magma_cube", EntityTypes.MAGMA_CUBE));
        define(new NpcTypeImpl.Builder("mooshroom", EntityTypes.MOOSHROOM));
        define(new NpcTypeImpl.Builder("mule", EntityTypes.MULE));
        define(new NpcTypeImpl.Builder("ocelot", EntityTypes.OCELOT));
        define(new NpcTypeImpl.Builder("pig", EntityTypes.PIG));
        define(new NpcTypeImpl.Builder("rabbit", EntityTypes.RABBIT));
        define(new NpcTypeImpl.Builder("sheep", EntityTypes.SHEEP));
        define(new NpcTypeImpl.Builder("silverfish", EntityTypes.SILVERFISH));
        define(new NpcTypeImpl.Builder("skeleton", EntityTypes.SKELETON));
        define(new NpcTypeImpl.Builder("skeleton_horse", EntityTypes.SKELETON_HORSE));
        define(new NpcTypeImpl.Builder("slime", EntityTypes.SLIME));
        define(new NpcTypeImpl.Builder("snow_golem", EntityTypes.SNOW_GOLEM));
        define(new NpcTypeImpl.Builder("spider", EntityTypes.SPIDER));
        define(new NpcTypeImpl.Builder("squid", EntityTypes.SQUID));
        define(new NpcTypeImpl.Builder("villager", EntityTypes.VILLAGER));
        define(new NpcTypeImpl.Builder("witch", EntityTypes.WITCH));
        define(new NpcTypeImpl.Builder("wither", EntityTypes.WITHER));
        define(new NpcTypeImpl.Builder("wither_skeleton", EntityTypes.WITHER_SKELETON));
        define(new NpcTypeImpl.Builder("wolf", EntityTypes.WOLF));
        define(new NpcTypeImpl.Builder("zombie", EntityTypes.ZOMBIE));
        define(new NpcTypeImpl.Builder("zombie_horse", EntityTypes.ZOMBIE_HORSE));
        define(new NpcTypeImpl.Builder("zombie_villager", EntityTypes.ZOMBIE_VILLAGER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_9)) return;
        define(new NpcTypeImpl.Builder("shulker", EntityTypes.SHULKER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_10)) return;
        define(new NpcTypeImpl.Builder("husk", EntityTypes.HUSK));
        define(new NpcTypeImpl.Builder("polar_bear", EntityTypes.POLAR_BEAR));
        define(new NpcTypeImpl.Builder("stray", EntityTypes.STRAY));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_11)) return;
        define(new NpcTypeImpl.Builder("evoker", EntityTypes.EVOKER));
        define(new NpcTypeImpl.Builder("llama", EntityTypes.LLAMA));
        define(new NpcTypeImpl.Builder("vex", EntityTypes.VEX));
        define(new NpcTypeImpl.Builder("vindicator", EntityTypes.VINDICATOR));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_12)) return;
        define(new NpcTypeImpl.Builder("illusioner", EntityTypes.ILLUSIONER));
        define(new NpcTypeImpl.Builder("parrot", EntityTypes.PARROT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_13)) return;
        define(new NpcTypeImpl.Builder("cod", EntityTypes.COD));
        define(new NpcTypeImpl.Builder("dolphin", EntityTypes.DOLPHIN));
        define(new NpcTypeImpl.Builder("drowned", EntityTypes.DROWNED));
        define(new NpcTypeImpl.Builder("phantom", EntityTypes.PHANTOM));
        define(new NpcTypeImpl.Builder("pufferfish", EntityTypes.PUFFERFISH));
        define(new NpcTypeImpl.Builder("salmon", EntityTypes.SALMON));
        define(new NpcTypeImpl.Builder("tropical_fish", EntityTypes.TROPICAL_FISH));
        define(new NpcTypeImpl.Builder("turtle", EntityTypes.TURTLE));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_14)) return;
        define(new NpcTypeImpl.Builder("fox", EntityTypes.FOX));
        define(new NpcTypeImpl.Builder("panda", EntityTypes.PANDA));
        define(new NpcTypeImpl.Builder("pillager", EntityTypes.PILLAGER));
        define(new NpcTypeImpl.Builder("ravager", EntityTypes.RAVAGER));
        define(new NpcTypeImpl.Builder("trader_llama", EntityTypes.TRADER_LLAMA));
        define(new NpcTypeImpl.Builder("wandering_trader", EntityTypes.WANDERING_TRADER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_15)) return;
        define(new NpcTypeImpl.Builder("bee", EntityTypes.BEE));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_16)) return;
        define(new NpcTypeImpl.Builder("hoglin", EntityTypes.HOGLIN));
        define(new NpcTypeImpl.Builder("piglin", EntityTypes.PIGLIN));
        define(new NpcTypeImpl.Builder("piglin_brute", EntityTypes.PIGLIN_BRUTE));
        define(new NpcTypeImpl.Builder("strider", EntityTypes.STRIDER));
        define(new NpcTypeImpl.Builder("zoglin", EntityTypes.ZOGLIN));
        define(new NpcTypeImpl.Builder("zombified_piglin", EntityTypes.ZOMBIFIED_PIGLIN));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_17)) return;
        define(new NpcTypeImpl.Builder("axolotl", EntityTypes.AXOLOTL));
        define(new NpcTypeImpl.Builder("glow_squid", EntityTypes.GLOW_SQUID));
        define(new NpcTypeImpl.Builder("goat", EntityTypes.GOAT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_19)) return;
        define(new NpcTypeImpl.Builder("allay", EntityTypes.ALLAY));
        define(new NpcTypeImpl.Builder("frog", EntityTypes.FROG));
        define(new NpcTypeImpl.Builder("tadpole", EntityTypes.TADPOLE));
        define(new NpcTypeImpl.Builder("warden", EntityTypes.WARDEN));
    }

    public Collection<NpcTypeImpl> getAll() {
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
