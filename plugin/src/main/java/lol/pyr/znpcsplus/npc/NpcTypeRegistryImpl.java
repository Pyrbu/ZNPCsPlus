package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
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

    public void registerDefault(PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry) {
        ServerVersion version = packetEvents.getServerManager().getVersion();

        register(new NpcTypeImpl.Builder(propertyRegistry, "player", EntityTypes.PLAYER).setHologramOffset(-0.15D)
                .addProperties(propertyRegistry.getByName("skin"),  propertyRegistry.getByName("skin_layers")));

        register(new NpcTypeImpl.Builder(propertyRegistry, "armor_stand", EntityTypes.ARMOR_STAND));
        register(new NpcTypeImpl.Builder(propertyRegistry, "bat", EntityTypes.BAT).setHologramOffset(-1.365));
        register(new NpcTypeImpl.Builder(propertyRegistry, "blaze", EntityTypes.BLAZE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "cat", EntityTypes.CAT));
        register(new NpcTypeImpl.Builder(propertyRegistry, "cave_spider", EntityTypes.CAVE_SPIDER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "chicken", EntityTypes.CHICKEN));
        register(new NpcTypeImpl.Builder(propertyRegistry, "cow", EntityTypes.COW));
        register(new NpcTypeImpl.Builder(propertyRegistry, "creeper", EntityTypes.CREEPER).setHologramOffset(-0.3D));
        register(new NpcTypeImpl.Builder(propertyRegistry, "donkey", EntityTypes.DONKEY));
        register(new NpcTypeImpl.Builder(propertyRegistry, "elder_guardian", EntityTypes.ELDER_GUARDIAN));
        register(new NpcTypeImpl.Builder(propertyRegistry, "ender_dragon", EntityTypes.ENDER_DRAGON));
        register(new NpcTypeImpl.Builder(propertyRegistry, "enderman", EntityTypes.ENDERMAN));
        register(new NpcTypeImpl.Builder(propertyRegistry, "endermite", EntityTypes.ENDERMITE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "ghast", EntityTypes.GHAST));
        register(new NpcTypeImpl.Builder(propertyRegistry, "giant", EntityTypes.GIANT));
        register(new NpcTypeImpl.Builder(propertyRegistry, "guardian", EntityTypes.GUARDIAN));
        register(new NpcTypeImpl.Builder(propertyRegistry, "horse", EntityTypes.HORSE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "iron_golem", EntityTypes.IRON_GOLEM));
        register(new NpcTypeImpl.Builder(propertyRegistry, "magma_cube", EntityTypes.MAGMA_CUBE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "mooshroom", EntityTypes.MOOSHROOM));
        register(new NpcTypeImpl.Builder(propertyRegistry, "mule", EntityTypes.MULE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "ocelot", EntityTypes.OCELOT));
        register(new NpcTypeImpl.Builder(propertyRegistry, "pig", EntityTypes.PIG));
        register(new NpcTypeImpl.Builder(propertyRegistry, "rabbit", EntityTypes.RABBIT));
        register(new NpcTypeImpl.Builder(propertyRegistry, "sheep", EntityTypes.SHEEP));
        register(new NpcTypeImpl.Builder(propertyRegistry, "silverfish", EntityTypes.SILVERFISH));
        register(new NpcTypeImpl.Builder(propertyRegistry, "skeleton", EntityTypes.SKELETON));
        register(new NpcTypeImpl.Builder(propertyRegistry, "skeleton_horse", EntityTypes.SKELETON_HORSE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "slime", EntityTypes.SLIME));
        register(new NpcTypeImpl.Builder(propertyRegistry, "snow_golem", EntityTypes.SNOW_GOLEM));
        register(new NpcTypeImpl.Builder(propertyRegistry, "spider", EntityTypes.SPIDER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "squid", EntityTypes.SQUID));
        register(new NpcTypeImpl.Builder(propertyRegistry, "villager", EntityTypes.VILLAGER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "witch", EntityTypes.WITCH));
        register(new NpcTypeImpl.Builder(propertyRegistry, "wither", EntityTypes.WITHER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "wither_skeleton", EntityTypes.WITHER_SKELETON));
        register(new NpcTypeImpl.Builder(propertyRegistry, "wolf", EntityTypes.WOLF));
        register(new NpcTypeImpl.Builder(propertyRegistry, "zombie", EntityTypes.ZOMBIE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "zombie_horse", EntityTypes.ZOMBIE_HORSE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "zombie_villager", EntityTypes.ZOMBIE_VILLAGER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_9)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "shulker", EntityTypes.SHULKER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_10)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "husk", EntityTypes.HUSK));
        register(new NpcTypeImpl.Builder(propertyRegistry, "polar_bear", EntityTypes.POLAR_BEAR));
        register(new NpcTypeImpl.Builder(propertyRegistry, "stray", EntityTypes.STRAY));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_11)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "evoker", EntityTypes.EVOKER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "llama", EntityTypes.LLAMA));
        register(new NpcTypeImpl.Builder(propertyRegistry, "vex", EntityTypes.VEX));
        register(new NpcTypeImpl.Builder(propertyRegistry, "vindicator", EntityTypes.VINDICATOR));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_12)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "illusioner", EntityTypes.ILLUSIONER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "parrot", EntityTypes.PARROT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_13)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "cod", EntityTypes.COD));
        register(new NpcTypeImpl.Builder(propertyRegistry, "dolphin", EntityTypes.DOLPHIN));
        register(new NpcTypeImpl.Builder(propertyRegistry, "drowned", EntityTypes.DROWNED));
        register(new NpcTypeImpl.Builder(propertyRegistry, "phantom", EntityTypes.PHANTOM));
        register(new NpcTypeImpl.Builder(propertyRegistry, "pufferfish", EntityTypes.PUFFERFISH));
        register(new NpcTypeImpl.Builder(propertyRegistry, "salmon", EntityTypes.SALMON));
        register(new NpcTypeImpl.Builder(propertyRegistry, "tropical_fish", EntityTypes.TROPICAL_FISH));
        register(new NpcTypeImpl.Builder(propertyRegistry, "turtle", EntityTypes.TURTLE));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_14)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "fox", EntityTypes.FOX));
        register(new NpcTypeImpl.Builder(propertyRegistry, "panda", EntityTypes.PANDA));
        register(new NpcTypeImpl.Builder(propertyRegistry, "pillager", EntityTypes.PILLAGER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "ravager", EntityTypes.RAVAGER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "trader_llama", EntityTypes.TRADER_LLAMA));
        register(new NpcTypeImpl.Builder(propertyRegistry, "wandering_trader", EntityTypes.WANDERING_TRADER));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_15)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "bee", EntityTypes.BEE));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_16)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "hoglin", EntityTypes.HOGLIN));
        register(new NpcTypeImpl.Builder(propertyRegistry, "piglin", EntityTypes.PIGLIN));
        register(new NpcTypeImpl.Builder(propertyRegistry, "piglin_brute", EntityTypes.PIGLIN_BRUTE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "strider", EntityTypes.STRIDER));
        register(new NpcTypeImpl.Builder(propertyRegistry, "zoglin", EntityTypes.ZOGLIN));
        register(new NpcTypeImpl.Builder(propertyRegistry, "zombified_piglin", EntityTypes.ZOMBIFIED_PIGLIN));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_17)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "axolotl", EntityTypes.AXOLOTL));
        register(new NpcTypeImpl.Builder(propertyRegistry, "glow_squid", EntityTypes.GLOW_SQUID));
        register(new NpcTypeImpl.Builder(propertyRegistry, "goat", EntityTypes.GOAT));

        if (!version.isNewerThanOrEquals(ServerVersion.V_1_19)) return;
        register(new NpcTypeImpl.Builder(propertyRegistry, "allay", EntityTypes.ALLAY));
        register(new NpcTypeImpl.Builder(propertyRegistry, "frog", EntityTypes.FROG));
        register(new NpcTypeImpl.Builder(propertyRegistry, "tadpole", EntityTypes.TADPOLE));
        register(new NpcTypeImpl.Builder(propertyRegistry, "warden", EntityTypes.WARDEN));
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
