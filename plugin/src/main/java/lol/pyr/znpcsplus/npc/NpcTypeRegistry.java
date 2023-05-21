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

    private NpcTypeImpl register(NpcTypeImpl.Builder builder) {
        return register(builder.build());
    }

    private NpcTypeImpl register(NpcTypeImpl type) {
        types.add(type);
        return type;
    }

    public void registerDefault(PacketEventsAPI<Plugin> packetEvents) {
        ServerVersion version = packetEvents.getServerManager().getVersion();

        register(new NpcTypeImpl.Builder("player", EntityTypes.PLAYER).setHologramOffset(-0.15D)
                .addProperties(EntityPropertyImpl.SKIN, EntityPropertyImpl.SKIN_LAYERS));

        register(new NpcTypeImpl.Builder("armor_stand", EntityTypes.ARMOR_STAND));
        register(new NpcTypeImpl.Builder("bat", EntityTypes.BAT).setHologramOffset(-1.365));
        register(new NpcTypeImpl.Builder("blaze", EntityTypes.BLAZE));
        register(new NpcTypeImpl.Builder("cat", EntityTypes.CAT));
        register(new NpcTypeImpl.Builder("cave_spider", EntityTypes.CAVE_SPIDER));
        register(new NpcTypeImpl.Builder("chicken", EntityTypes.CHICKEN));
        register(new NpcTypeImpl.Builder("cow", EntityTypes.COW));
        register(new NpcTypeImpl.Builder("creeper", EntityTypes.CREEPER).setHologramOffset(-0.3D));
        register(new NpcTypeImpl.Builder("donkey", EntityTypes.DONKEY));
        register(new NpcTypeImpl.Builder("elder_guardian", EntityTypes.ELDER_GUARDIAN));
        register(new NpcTypeImpl.Builder("ender_dragon", EntityTypes.ENDER_DRAGON));
        register(new NpcTypeImpl.Builder("enderman", EntityTypes.ENDERMAN));
        register(new NpcTypeImpl.Builder("endermite", EntityTypes.ENDERMITE));
        register(new NpcTypeImpl.Builder("ghast", EntityTypes.GHAST));
        register(new NpcTypeImpl.Builder("giant", EntityTypes.GIANT));
        register(new NpcTypeImpl.Builder("guardian", EntityTypes.GUARDIAN));
        register(new NpcTypeImpl.Builder("horse", EntityTypes.HORSE));
        register(new NpcTypeImpl.Builder("iron_golem", EntityTypes.IRON_GOLEM));
        register(new NpcTypeImpl.Builder("magma_cube", EntityTypes.MAGMA_CUBE));
        register(new NpcTypeImpl.Builder("mooshroom", EntityTypes.MOOSHROOM));
        register(new NpcTypeImpl.Builder("mule", EntityTypes.MULE));
        register(new NpcTypeImpl.Builder("ocelot", EntityTypes.OCELOT));
        register(new NpcTypeImpl.Builder("pig", EntityTypes.PIG));
        register(new NpcTypeImpl.Builder("rabbit", EntityTypes.RABBIT));
        register(new NpcTypeImpl.Builder("sheep", EntityTypes.SHEEP));
        register(new NpcTypeImpl.Builder("silverfish", EntityTypes.SILVERFISH));
        register(new NpcTypeImpl.Builder("skeleton", EntityTypes.SKELETON));
        register(new NpcTypeImpl.Builder("skeleton_horse", EntityTypes.SKELETON_HORSE));
        register(new NpcTypeImpl.Builder("slime", EntityTypes.SLIME));
        register(new NpcTypeImpl.Builder("snow_golem", EntityTypes.SNOW_GOLEM));
        register(new NpcTypeImpl.Builder("spider", EntityTypes.SPIDER));
        register(new NpcTypeImpl.Builder("squid", EntityTypes.SQUID));
        register(new NpcTypeImpl.Builder("villager", EntityTypes.VILLAGER));
        register(new NpcTypeImpl.Builder("witch", EntityTypes.WITCH));
        register(new NpcTypeImpl.Builder("wither", EntityTypes.WITHER));
        register(new NpcTypeImpl.Builder("wither_skeleton", EntityTypes.WITHER_SKELETON));
        register(new NpcTypeImpl.Builder("wolf", EntityTypes.WOLF));
        register(new NpcTypeImpl.Builder("zombie", EntityTypes.ZOMBIE));
        register(new NpcTypeImpl.Builder("zombie_horse", EntityTypes.ZOMBIE_HORSE));
        register(new NpcTypeImpl.Builder("zombie_villager", EntityTypes.ZOMBIE_VILLAGER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_9)) return;
        register(new NpcTypeImpl.Builder("shulker", EntityTypes.SHULKER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_10)) return;
        register(new NpcTypeImpl.Builder("husk", EntityTypes.HUSK));
        register(new NpcTypeImpl.Builder("polar_bear", EntityTypes.POLAR_BEAR));
        register(new NpcTypeImpl.Builder("stray", EntityTypes.STRAY));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_11)) return;
        register(new NpcTypeImpl.Builder("evoker", EntityTypes.EVOKER));
        register(new NpcTypeImpl.Builder("llama", EntityTypes.LLAMA));
        register(new NpcTypeImpl.Builder("vex", EntityTypes.VEX));
        register(new NpcTypeImpl.Builder("vindicator", EntityTypes.VINDICATOR));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_12)) return;
        register(new NpcTypeImpl.Builder("illusioner", EntityTypes.ILLUSIONER));
        register(new NpcTypeImpl.Builder("parrot", EntityTypes.PARROT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_13)) return;
        register(new NpcTypeImpl.Builder("cod", EntityTypes.COD));
        register(new NpcTypeImpl.Builder("dolphin", EntityTypes.DOLPHIN));
        register(new NpcTypeImpl.Builder("drowned", EntityTypes.DROWNED));
        register(new NpcTypeImpl.Builder("phantom", EntityTypes.PHANTOM));
        register(new NpcTypeImpl.Builder("pufferfish", EntityTypes.PUFFERFISH));
        register(new NpcTypeImpl.Builder("salmon", EntityTypes.SALMON));
        register(new NpcTypeImpl.Builder("tropical_fish", EntityTypes.TROPICAL_FISH));
        register(new NpcTypeImpl.Builder("turtle", EntityTypes.TURTLE));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_14)) return;
        register(new NpcTypeImpl.Builder("fox", EntityTypes.FOX));
        register(new NpcTypeImpl.Builder("panda", EntityTypes.PANDA));
        register(new NpcTypeImpl.Builder("pillager", EntityTypes.PILLAGER));
        register(new NpcTypeImpl.Builder("ravager", EntityTypes.RAVAGER));
        register(new NpcTypeImpl.Builder("trader_llama", EntityTypes.TRADER_LLAMA));
        register(new NpcTypeImpl.Builder("wandering_trader", EntityTypes.WANDERING_TRADER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_15)) return;
        register(new NpcTypeImpl.Builder("bee", EntityTypes.BEE));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_16)) return;
        register(new NpcTypeImpl.Builder("hoglin", EntityTypes.HOGLIN));
        register(new NpcTypeImpl.Builder("piglin", EntityTypes.PIGLIN));
        register(new NpcTypeImpl.Builder("piglin_brute", EntityTypes.PIGLIN_BRUTE));
        register(new NpcTypeImpl.Builder("strider", EntityTypes.STRIDER));
        register(new NpcTypeImpl.Builder("zoglin", EntityTypes.ZOGLIN));
        register(new NpcTypeImpl.Builder("zombified_piglin", EntityTypes.ZOMBIFIED_PIGLIN));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_17)) return;
        register(new NpcTypeImpl.Builder("axolotl", EntityTypes.AXOLOTL));
        register(new NpcTypeImpl.Builder("glow_squid", EntityTypes.GLOW_SQUID));
        register(new NpcTypeImpl.Builder("goat", EntityTypes.GOAT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_19)) return;
        register(new NpcTypeImpl.Builder("allay", EntityTypes.ALLAY));
        register(new NpcTypeImpl.Builder("frog", EntityTypes.FROG));
        register(new NpcTypeImpl.Builder("tadpole", EntityTypes.TADPOLE));
        register(new NpcTypeImpl.Builder("warden", EntityTypes.WARDEN));
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
