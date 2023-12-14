package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.properties.*;
import lol.pyr.znpcsplus.entity.properties.villager.VillagerLevelProperty;
import lol.pyr.znpcsplus.entity.properties.villager.VillagerProfessionProperty;
import lol.pyr.znpcsplus.entity.properties.villager.VillagerTypeProperty;
import lol.pyr.znpcsplus.entity.serializers.*;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.util.*;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.DyeColor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 1.8  <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=7415">...</a>
 * 1.9  <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=7968">...</a>
 * 1.10 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=8241">...</a>
 * 1.11 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=8534">...</a>
 * 1.12 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=14048">...</a>
 * 1.13 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=14800">...</a>
 * 1.14 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=15240">...</a>
 * 1.15 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=15991">...</a>
 * 1.16 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=16539">...</a>
 * 1.17 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=17521">...</a>
 * 1.18-1.19 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=18191">...</a>
 * 1.20 <a href="https://wiki.vg/index.php?title=Entity_metadata">...</a>
 */
@SuppressWarnings("unchecked")
public class EntityPropertyRegistryImpl implements EntityPropertyRegistry {
    private final Map<Class<?>, PropertySerializer<?>> serializerMap = new HashMap<>();
    private final Map<String, EntityPropertyImpl<?>> byName = new HashMap<>();
    private final ConfigManager configManager;

    public EntityPropertyRegistryImpl(MojangSkinCache skinCache, ConfigManager configManager) {
        registerSerializer(new ComponentPropertySerializer());
        registerSerializer(new NamedColorPropertySerializer());
        registerSerializer(new SkinDescriptorSerializer(skinCache));
        registerSerializer(new ItemStackPropertySerializer());
        registerSerializer(new ColorPropertySerializer());
        registerSerializer(new Vector3fPropertySerializer());
        registerSerializer(new BlockStatePropertySerializer());
        registerSerializer(new LookTypeSerializer());
        registerSerializer(new GenericSerializer<>(Vector3i::toString, Vector3i::fromString, Vector3i.class));

        registerEnumSerializer(NpcPose.class);
        registerEnumSerializer(DyeColor.class);
        registerEnumSerializer(CatVariant.class);
        registerEnumSerializer(CreeperState.class);
        registerEnumSerializer(ParrotVariant.class);
        registerEnumSerializer(SpellType.class);
        registerEnumSerializer(FoxVariant.class);
        registerEnumSerializer(FrogVariant.class);
        registerEnumSerializer(VillagerType.class);
        registerEnumSerializer(VillagerProfession.class);
        registerEnumSerializer(VillagerLevel.class);
        registerEnumSerializer(AxolotlVariant.class);
        registerEnumSerializer(HorseType.class);
        registerEnumSerializer(HorseColor.class);
        registerEnumSerializer(HorseStyle.class);
        registerEnumSerializer(HorseArmor.class);
        registerEnumSerializer(LlamaVariant.class);
        registerEnumSerializer(MooshroomVariant.class);
        registerEnumSerializer(OcelotType.class);
        registerEnumSerializer(PandaGene.class);
        registerEnumSerializer(PuffState.class);
        registerEnumSerializer(TropicalFishVariant.TropicalFishPattern.class);
        registerEnumSerializer(SnifferState.class);
        registerEnumSerializer(RabbitType.class);
        registerEnumSerializer(AttachDirection.class);

        registerPrimitiveSerializers(Integer.class, Boolean.class, Double.class, Float.class, Long.class, Short.class, Byte.class, String.class);

        this.configManager = configManager;

        /*
        registerType("using_item", false); // TODO: fix it for 1.8 and add new property to use offhand item and riptide animation

        // End Crystal
        registerType("beam_target", null); // TODO: Make a block pos class for this
        registerType("show_base", true); // TODO

        // Enderman
        registerType("enderman_held_block", new BlockState(0)); // TODO: figure out the type on this
        registerType("enderman_screaming", false); // TODO
        registerType("enderman_staring", false); // TODO

        // Guardian
        registerType("is_elder", false); // TODO: ensure it only works till 1.10. Note: index is wrong on wiki.vg

         */
    }

    public void registerTypes(PacketFactory packetFactory, LegacyComponentSerializer textSerializer) {
        ServerVersion ver = PacketEvents.getAPI().getServerManager().getVersion();
        boolean legacyBooleans = ver.isOlderThan(ServerVersion.V_1_9);
        boolean legacyNames = ver.isOlderThan(ServerVersion.V_1_9);
        boolean optionalComponents = ver.isNewerThanOrEquals(ServerVersion.V_1_13);

        register(new EquipmentProperty(packetFactory, "helmet", EquipmentSlot.HELMET));
        register(new EquipmentProperty(packetFactory, "chestplate", EquipmentSlot.CHEST_PLATE));
        register(new EquipmentProperty(packetFactory, "leggings", EquipmentSlot.LEGGINGS));
        register(new EquipmentProperty(packetFactory, "boots", EquipmentSlot.BOOTS));
        register(new EquipmentProperty(packetFactory, "hand", EquipmentSlot.MAIN_HAND));
        register(new EquipmentProperty(packetFactory, "offhand", EquipmentSlot.OFF_HAND));

        register(new NameProperty(textSerializer, legacyNames, optionalComponents));
        register(new DummyProperty<>("display_name", String.class));
        register(new DinnerboneProperty(legacyNames, optionalComponents));

        register(new DummyProperty<>("look", LookType.FIXED));
        register(new DummyProperty<>("look_distance", configManager.getConfig().lookPropertyDistance()));
        register(new DummyProperty<>("view_distance", configManager.getConfig().viewDistance()));

        register(new DummyProperty<>("permission_required", false));

        register(new GlowProperty(packetFactory));
        register(new BitsetProperty("fire", 0, 0x01));
        register(new BitsetProperty("invisible", 0, 0x20));
        register(new HologramItemProperty());
        linkProperties("glow", "fire", "invisible");
        register(new BooleanProperty("silent", 4, false, legacyBooleans));

        final int tameableIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) tameableIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) tameableIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) tameableIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) tameableIndex = 13;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) tameableIndex = 12;
        else tameableIndex = 16;
        register(new BitsetProperty("sitting", tameableIndex, 0x01));
        register(new BitsetProperty("tamed", tameableIndex, 0x04));

        int potionIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) potionIndex = 10;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) potionIndex = 9;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) potionIndex = 8;
        else potionIndex = 7;
        register(new EncodedIntegerProperty<>("potion_color", Color.class, potionIndex++, Color::asRGB));
        register(new BooleanProperty("potion_ambient", potionIndex, false, legacyBooleans));

        int babyIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) babyIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) babyIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) babyIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) babyIndex = 12;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) babyIndex = 11;
        else babyIndex = 12;
        register(new BooleanProperty("baby", babyIndex, false, legacyBooleans));

        // Player
        register(new DummyProperty<>("skin", SkinDescriptor.class, false));
        final int skinLayersIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) skinLayersIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_16)) skinLayersIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) skinLayersIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) skinLayersIndex = 13;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) skinLayersIndex = 12;
        else skinLayersIndex = 10;
        register(new BitsetProperty("skin_cape", skinLayersIndex, 0x01));
        register(new BitsetProperty("skin_jacket", skinLayersIndex, 0x02));
        register(new BitsetProperty("skin_left_sleeve", skinLayersIndex, 0x04));
        register(new BitsetProperty("skin_right_sleeve", skinLayersIndex, 0x08));
        register(new BitsetProperty("skin_left_leg", skinLayersIndex, 0x10));
        register(new BitsetProperty("skin_right_leg", skinLayersIndex, 0x20));
        register(new BitsetProperty("skin_hat", skinLayersIndex, 0x40));
        linkProperties("skin_cape", "skin_jacket", "skin_left_sleeve", "skin_right_sleeve", "skin_left_leg", "skin_right_leg", "skin_hat");

        // Armor Stand
        int armorStandIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) armorStandIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) armorStandIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) armorStandIndex = 13;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) armorStandIndex = 11;
        else armorStandIndex = 10;
        register(new BitsetProperty("small", armorStandIndex, 0x01));
        register(new BitsetProperty("arms", armorStandIndex, 0x04));
        register(new BitsetProperty("base_plate", armorStandIndex++, 0x08, true));
        linkProperties("small", "arms", "base_plate");
        register(new RotationProperty("head_rotation", armorStandIndex++, Vector3f.zero()));
        register(new RotationProperty("body_rotation", armorStandIndex++, Vector3f.zero()));
        register(new RotationProperty("left_arm_rotation", armorStandIndex++, new Vector3f(-10, 0, -10)));
        register(new RotationProperty("right_arm_rotation", armorStandIndex++, new Vector3f(-15, 0, 10)));
        register(new RotationProperty("left_leg_rotation", armorStandIndex++, new Vector3f(-1, 0, -1)));
        register(new RotationProperty("right_leg_rotation", armorStandIndex, new Vector3f(1, 0, 1)));

        // Ghast
        final int ghastAttackingIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) ghastAttackingIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) ghastAttackingIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) ghastAttackingIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) ghastAttackingIndex = 12;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) ghastAttackingIndex = 11;
        else ghastAttackingIndex = 16;
        register(new BooleanProperty("attacking", ghastAttackingIndex, false, legacyBooleans));

        // Bat
        final int batIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) batIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) batIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) batIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) batIndex = 12;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) batIndex = 11;
        else batIndex = 16;
        register(new BooleanProperty("hanging", batIndex, false, true /* This isn't a mistake */));

        // Blaze
        final int blazeIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) blazeIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) blazeIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) blazeIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) blazeIndex = 12;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) blazeIndex = 11;
        else blazeIndex = 16;
        register(new BitsetProperty("blaze_on_fire", blazeIndex, 0x01));

        // Creeper
        int creeperIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) creeperIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) creeperIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) creeperIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) creeperIndex = 12;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) creeperIndex = 11;
        else creeperIndex= 16;
        register(new EncodedIntegerProperty<>("creeper_state", CreeperState.IDLE, creeperIndex++, CreeperState::getState));
        register(new BooleanProperty("creeper_charged", creeperIndex, false, legacyBooleans));

        // Abstract Horse
        int horseIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) horseIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) horseIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) horseIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) horseIndex = 13;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) horseIndex = 12;
        else horseIndex = 16;
        int horseEating = ver.isNewerThanOrEquals(ServerVersion.V_1_12) ? 0x10 : 0x20;
        register(new BitsetProperty("is_tame", horseIndex, 0x02, false, legacyBooleans));
        register(new BitsetProperty("is_saddled", horseIndex, 0x04, false, legacyBooleans));
        register(new BitsetProperty("is_eating", horseIndex, horseEating, false, legacyBooleans));
        register(new BitsetProperty("is_rearing", horseIndex, horseEating << 1, false, legacyBooleans));
        register(new BitsetProperty("has_mouth_open", horseIndex, horseEating << 2, false, legacyBooleans));

        // End Crystal
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            int endCrystalIndex;
            if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) endCrystalIndex = 8;
            else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) endCrystalIndex = 7;
            else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) endCrystalIndex = 6;
            else endCrystalIndex = 5;
            register(new OptionalBlockPosProperty("beam_target", null, endCrystalIndex++));
            register(new BooleanProperty("show_base", endCrystalIndex, true, false));
        }

        // Guardian
        if (ver.isOlderThan(ServerVersion.V_1_11)) {
            int guardianIndex;
            if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) guardianIndex = 12;
            else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) guardianIndex = 11;
            else guardianIndex = 16;
            register(new BitsetProperty("is_elder", guardianIndex, 0x04, false, legacyBooleans));
            register(new BitsetProperty("is_retracting_spikes", guardianIndex, 0x02, false, legacyBooleans));
            linkProperties("is_elder", "is_retracting_spikes");
            // TODO: add guardian beam target
        } else {
            int guardianIndex;
            if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) guardianIndex = 16;
            else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) guardianIndex = 15;
            else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) guardianIndex = 14;
            else guardianIndex = 12;
            register(new BooleanProperty("is_retracting_spikes", guardianIndex, false, false));
        }

        // Horse
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_8) && ver.isOlderThan(ServerVersion.V_1_9)) {
            register(new EncodedByteProperty<>("horse_type", HorseType.HORSE, 19, obj -> (byte) obj.ordinal()));
        } else if (ver.isOlderThan(ServerVersion.V_1_11)) {
            int horseTypeIndex = 14;
            if (ver.isOlderThan(ServerVersion.V_1_10)) horseTypeIndex = 13;
            register(new EncodedIntegerProperty<>("horse_type", HorseType.HORSE, horseTypeIndex, Enum::ordinal));
        }
        int horseVariantIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_18)) horseVariantIndex = 18;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) horseVariantIndex = 19;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) horseVariantIndex = 18;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) horseVariantIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) horseVariantIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) horseVariantIndex = 14;
        else horseVariantIndex = 20;
        register(new HorseStyleProperty(horseVariantIndex));
        register(new HorseColorProperty(horseVariantIndex));
        linkProperties("horse_style", "horse_color");

        // Use chesteplate property for 1.14 and above
        if (ver.isOlderThan(ServerVersion.V_1_14)) {
            int horseArmorIndex;
            if (ver.isNewerThanOrEquals(ServerVersion.V_1_11)) horseArmorIndex = 16;
            else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) horseArmorIndex = 17;
            else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) horseArmorIndex = 16;
            else horseArmorIndex = 22;
            register(new EncodedIntegerProperty<>("horse_armor", HorseArmor.NONE, horseArmorIndex, Enum::ordinal));
        }

        // Chested Horse
        if (ver.isOlderThan(ServerVersion.V_1_11)) {
            register(new BitsetProperty("has_chest", horseIndex, 0x08, false, legacyBooleans));
            linkProperties("is_saddled", "has_chest", "is_eating", "is_rearing", "has_mouth_open");
        } else {
            register(new BooleanProperty("has_chest", horseVariantIndex, false, legacyBooleans));
            linkProperties("is_saddled", "is_eating", "is_rearing", "has_mouth_open");
        }

        // Slime, Magma Cube and Phantom
        int sizeIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) sizeIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) sizeIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) sizeIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) sizeIndex = 12;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) sizeIndex = 11;
        else sizeIndex = 16;
        register(new IntegerProperty("size", sizeIndex, 1, legacyBooleans));

        // Ocelot
        if (ver.isOlderThan(ServerVersion.V_1_14)) {
            int ocelotIndex;
            if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) ocelotIndex = 15;
            else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) ocelotIndex = 14;
            else ocelotIndex = 18;
            if (legacyBooleans) register(new EncodedByteProperty<>("ocelot_type", OcelotType.OCELOT, ocelotIndex, obj -> (byte) obj.ordinal()));
            else register(new EncodedIntegerProperty<>("ocelot_type", OcelotType.OCELOT, ocelotIndex, Enum::ordinal));
        }

        // Pig
        int pigIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) pigIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) pigIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) pigIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) pigIndex = 13;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) pigIndex = 12;
        else pigIndex = 16;
        register(new BooleanProperty("pig_saddled", pigIndex, false, legacyBooleans));

        // Rabbit
        int rabbitIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) rabbitIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) rabbitIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) rabbitIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) rabbitIndex = 13;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) rabbitIndex = 12;
        else rabbitIndex = 18;
        register(new RabbitTypeProperty(rabbitIndex, legacyBooleans, legacyNames, optionalComponents));

        // Sheep
        int sheepIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) sheepIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) sheepIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) sheepIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) sheepIndex = 13;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) sheepIndex = 12;
        else sheepIndex = 16;
        // noinspection deprecation
        register(new EncodedByteProperty<>("sheep_color", DyeColor.WHITE, sheepIndex, DyeColor::getWoolData));
        register(new BitsetProperty("sheep_sheared", sheepIndex, 0x10, false, legacyBooleans)); // no need to link because sheep_sheared is only visible when sheep_color is WHITE

        // Wolf
        int wolfIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) wolfIndex = 19;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) wolfIndex = 18;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) wolfIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) wolfIndex = 15;
        else wolfIndex = 19;
        register(new BooleanProperty("wolf_begging", wolfIndex++, false, legacyBooleans));
        if (legacyBooleans) {
            // noinspection deprecation
            register(new EncodedByteProperty<>("wolf_collar", DyeColor.BLUE, wolfIndex++, DyeColor::getDyeData));
        } else register(new EncodedIntegerProperty<>("wolf_collar", DyeColor.RED, wolfIndex++, Enum::ordinal));
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            register(new EncodedIntegerProperty<>("wolf_angry", false, wolfIndex, b -> b ? 1 : 0));
            linkProperties("tamed", "sitting");
        }
        else {
            register(new BitsetProperty("wolf_angry", tameableIndex, 0x02));
            linkProperties("wolf_angry", "tamed", "sitting");
        }

        // Wither
        int witherIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) witherIndex = 16; // using the first index, so we can add the other properties later if needed
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) witherIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) witherIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) witherIndex = 12;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) witherIndex = 11;
        else witherIndex = 17;
        witherIndex += 3; // skip the first 3 indexes, will be used for the other properties later
        register(new IntegerProperty("invulnerable_time", witherIndex, 0, false));

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_9)) return;
        // Shulker
        int shulkerIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) shulkerIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) shulkerIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) shulkerIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) shulkerIndex = 12;
        else shulkerIndex = 11;
        register(new CustomTypeProperty<>("attach_direction", shulkerIndex++, AttachDirection.DOWN, EntityDataTypes.BLOCK_FACE, attachDir -> BlockFace.valueOf(attachDir.name())));
        register(new EncodedByteProperty<>("shield_height", 0, shulkerIndex++, value -> (byte) Math.max(0, Math.min(100, value))));
        // noinspection deprecation
        register(new EncodedByteProperty<>("shulker_color", DyeColor.class, shulkerIndex, DyeColor::getWoolData));

        // Snow Golem
        int snowGolemIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) snowGolemIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) snowGolemIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) snowGolemIndex = 14;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) snowGolemIndex = 12;
        else snowGolemIndex = 10;
        register(new CustomTypeProperty<>("derpy_snowgolem", snowGolemIndex, false, EntityDataTypes.BYTE, b -> (byte) (b ? 0x00 : 0x10)));

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_10)) return;
        // Polar Bear
        int polarBearIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) polarBearIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) polarBearIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) polarBearIndex = 15;
        else polarBearIndex = 13;
        register(new BooleanProperty("polar_bear_standing", polarBearIndex, false, false));

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_11)) return;
        // Spellcaster Illager
        int spellIndex = 12;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) spellIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) spellIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) spellIndex = 15;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_12)) spellIndex = 13;
        register(new EncodedByteProperty<>("spell", SpellType.NONE, spellIndex, obj -> (byte) Math.min(obj.ordinal(), ver.isOlderThan(ServerVersion.V_1_13) ? 3 : 5)));

        // Llama
        int llamaIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_18)) llamaIndex = 20;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) llamaIndex = 21;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) llamaIndex = 20;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) llamaIndex = 19;
        else llamaIndex = 17;
        register(new EncodedIntegerProperty<DyeColor>("carpet_color", DyeColor.class, llamaIndex++, obj -> obj == null ? -1 : obj.ordinal()));
        register(new EncodedIntegerProperty<>("llama_variant", LlamaVariant.CREAMY, llamaIndex, Enum::ordinal));

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_12)) return;
        // Parrot
        int parrotIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) parrotIndex = 19;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) parrotIndex = 18;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) parrotIndex = 17;
        else parrotIndex = 15;
        register(new EncodedIntegerProperty<>("parrot_variant", ParrotVariant.RED_BLUE, parrotIndex, Enum::ordinal));

        // Player
        NBTProperty.NBTDecoder<ParrotVariant> parrotVariantDecoder = (variant) -> {
            NBTCompound compound = new NBTCompound();
            if (variant == null) return compound;
            compound.setTag("id", new NBTString("minecraft:parrot"));
            compound.setTag("Variant", new NBTInt(variant.ordinal()));
            return compound;
        };
        int shoulderIndex = skinLayersIndex+2;
        register(new NBTProperty<>("shoulder_entity_left", ParrotVariant.class, shoulderIndex++, parrotVariantDecoder, true));
        register(new NBTProperty<>("shoulder_entity_right", ParrotVariant.class, shoulderIndex, parrotVariantDecoder, true));

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_13)) return;
        // Pufferfish
        int pufferfishIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) pufferfishIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) pufferfishIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) pufferfishIndex = 15;
        else pufferfishIndex = 13;
        register(new EncodedIntegerProperty<>("puff_state", PuffState.DEFLATED, pufferfishIndex, Enum::ordinal));

        // Tropical Fish
        int tropicalFishIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) tropicalFishIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) tropicalFishIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) tropicalFishIndex = 15;
        else tropicalFishIndex = 13;
        register(new TropicalFishVariantProperty<>("tropical_fish_pattern", TropicalFishVariant.TropicalFishPattern.KOB, tropicalFishIndex, TropicalFishVariant.Builder::pattern));
        register(new TropicalFishVariantProperty<>("tropical_fish_body_color", DyeColor.WHITE, tropicalFishIndex, TropicalFishVariant.Builder::bodyColor));
        register(new TropicalFishVariantProperty<>("tropical_fish_pattern_color", DyeColor.WHITE, tropicalFishIndex, TropicalFishVariant.Builder::patternColor));
        linkProperties("tropical_fish_pattern", "tropical_fish_body_color", "tropical_fish_pattern_color");

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_14)) return;
        // Pose
        register(new CustomTypeProperty<>("pose", 6, NpcPose.STANDING, EntityDataTypes.ENTITY_POSE, npcPose -> EntityPose.valueOf(npcPose.name())));

        // Villager
        final int villagerIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) villagerIndex = 18;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) villagerIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_14)) villagerIndex = 16;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_10)) villagerIndex = 13;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_9)) villagerIndex = 12;
        else villagerIndex = 16;
        register(new VillagerTypeProperty("villager_type", villagerIndex, VillagerType.PLAINS));
        register(new VillagerProfessionProperty("villager_profession", villagerIndex, VillagerProfession.NONE));
        register(new VillagerLevelProperty("villager_level", villagerIndex, VillagerLevel.STONE));
        linkProperties("villager_type", "villager_profession", "villager_level");

        // Cat
        int catIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) catIndex = 19;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) catIndex = 18;
        else catIndex = 17;
        register(new EncodedIntegerProperty<>("cat_variant", CatVariant.BLACK, catIndex++, Enum::ordinal, EntityDataTypes.CAT_VARIANT));
        register(new BooleanProperty("cat_laying", catIndex++, false, legacyBooleans));
        register(new BooleanProperty("cat_relaxed", catIndex++, false, legacyBooleans));
        register(new EncodedIntegerProperty<>("cat_collar", DyeColor.RED, catIndex, Enum::ordinal));

        // Fox
        int foxIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) foxIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) foxIndex = 16;
        else foxIndex = 15;
        register(new EncodedIntegerProperty<>("fox_variant", FoxVariant.RED, foxIndex++, Enum::ordinal));
        register(new BitsetProperty("fox_sitting", foxIndex, 0x01));
        register(new BitsetProperty("fox_crouching", foxIndex, 0x04));
        register(new BitsetProperty("fox_sleeping", foxIndex, 0x20));
        linkProperties("fox_sitting", "fox_crouching", "fox_sleeping");

        int mooshroomIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) mooshroomIndex = 17;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) mooshroomIndex = 16;
        else mooshroomIndex = 15;
        register(new EncodedStringProperty<>("mooshroom_variant", MooshroomVariant.RED, mooshroomIndex, MooshroomVariant::getVariantName));

        // Panda
        int pandaIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) pandaIndex = 20;
        else if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) pandaIndex = 19;
        else pandaIndex = 18;
        register(new EncodedByteProperty<>("panda_main_gene", PandaGene.NORMAL, pandaIndex++, obj -> (byte) obj.ordinal()));
        register(new EncodedByteProperty<>("panda_hidden_gene", PandaGene.NORMAL, pandaIndex++, obj -> (byte) obj.ordinal()));
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            register(new BitsetProperty("panda_sneezing", pandaIndex, 0x02));
            register(new BitsetProperty("panda_rolling", pandaIndex, 0x04));
            register(new BitsetProperty("panda_sitting", pandaIndex, 0x08));
            register(new BitsetProperty("panda_on_back", pandaIndex, 0x10));
            linkProperties("panda_sneezing", "panda_rolling", "panda_sitting", "panda_on_back");
        } else {
            register(new BitsetProperty("panda_sneezing", pandaIndex, 0x02));
            register(new BitsetProperty("panda_eating", pandaIndex, 0x04));
            linkProperties("panda_sneezing", "panda_eating");
        }


        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_15)) return;

        register(new BitsetProperty("fox_faceplanted", foxIndex, 0x40));
        linkProperties("fox_sitting", "fox_crouching", "fox_sleeping", "fox_faceplanted");

        // Bee
        int beeIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) beeIndex = 17;
        else beeIndex = 18;
        register(new BitsetProperty("has_nectar", beeIndex++, 0x08));
        register(new EncodedIntegerProperty<>("angry", false, beeIndex, enabled -> enabled ? 1 : 0));

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_16)) return;

        // Hoglin and Piglin Zombification
        final int zombificationIndex;
        if (ver.isNewerThanOrEquals(ServerVersion.V_1_17)) zombificationIndex = 17; // Change piglinIndex, pillagerIndex, striderIndex and vindicatorIndex if you change this
        else zombificationIndex = 16;
        register(new BooleanProperty("hoglin_immune_to_zombification", zombificationIndex, false, legacyBooleans));
        register(new BooleanProperty("piglin_immune_to_zombification", zombificationIndex-1, false, legacyBooleans));

        // Piglin
        int piglinIndex = zombificationIndex;
        register(new BooleanProperty("piglin_baby", piglinIndex++, false, legacyBooleans));
        register(new BooleanProperty("piglin_charging_crossbow", piglinIndex++, false, legacyBooleans));
        register(new BooleanProperty("piglin_dancing", piglinIndex, false, legacyBooleans));

        // Pillager
        register(new BooleanProperty("pillager_charging", zombificationIndex, false, legacyBooleans));

        // Strider
        int striderIndex = zombificationIndex + 1;
        register(new BooleanProperty("strider_shaking", striderIndex++, false, legacyBooleans)); // TODO: Fix this, it needs to be set constantly i guess
        register(new BooleanProperty("strider_saddled", striderIndex, false, legacyBooleans));

        // Vindicator
        int vindicatorIndex = zombificationIndex -1;
        register(new BooleanProperty("celebrating", vindicatorIndex, false, legacyBooleans));

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_17)) return;
        // Axolotl
        register(new EncodedIntegerProperty<>("axolotl_variant", AxolotlVariant.LUCY, 17, Enum::ordinal));
        register(new BooleanProperty("playing_dead", 18, false, legacyBooleans));

        // Goat
        register(new BooleanProperty("has_left_horn", 18, true, legacyBooleans));
        register(new BooleanProperty("has_right_horn", 19, true, legacyBooleans));

        register(new EncodedIntegerProperty<>("shaking", false,7, enabled -> enabled ? 140 : 0));
        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_19)) return;
        // Frog
        register(new EncodedIntegerProperty<>("frog_variant", FrogVariant.TEMPERATE, 17, Enum::ordinal, EntityDataTypes.FROG_VARIANT));

        if (!ver.isNewerThanOrEquals(ServerVersion.V_1_20)) return;

        // Camel
        register(new BooleanProperty("bashing", 18, false, legacyBooleans));

        // Sniffer
        register(new CustomTypeProperty<>("sniffer_state", 17, SnifferState.IDLING, EntityDataTypes.SNIFFER_STATE, state -> com.github.retrooper.packetevents.protocol.entity.sniffer.SnifferState.valueOf(state.name())));
    }

    private void registerSerializer(PropertySerializer<?> serializer) {
        serializerMap.put(serializer.getTypeClass(), serializer);
    }

    private <T extends Enum<T>> void registerEnumSerializer(Class<T> clazz) {
        serializerMap.put(clazz, new EnumPropertySerializer<>(clazz));
    }

    private void registerPrimitiveSerializers(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            registerPrimitiveSerializer(clazz);
        }
    }

    private <T> void registerPrimitiveSerializer(Class<T> clazz) {
        serializerMap.put(clazz, new PrimitivePropertySerializer<>(clazz));
    }

    private <T> void register(EntityPropertyImpl<?> property) {
        if (byName.containsKey(property.getName()))
            throw new IllegalArgumentException("Duplicate property name: " + property.getName());
        byName.put(property.getName(), property);
    }

    private void linkProperties(String... names) {
        linkProperties(Arrays.stream(names)
                .map(this::getByName)
                .collect(Collectors.toSet()));
    }

    private void linkProperties(Collection<EntityPropertyImpl<?>> properties) {
        for (EntityPropertyImpl<?> property : properties) for (EntityPropertyImpl<?> dependency : properties) {
            if (property.equals(dependency)) continue;
            property.addDependency(dependency);
        }
    }

    public <V> PropertySerializer<V> getSerializer(Class<V> type) {
        return (PropertySerializer<V>) serializerMap.get(type);
    }

    @Override
    public Collection<EntityProperty<?>> getAll() {
        return Collections.unmodifiableCollection(
                byName.values().stream()
                        .map(property -> (EntityProperty<?>) property)
                        .collect(Collectors.toSet()));
    }

    public <T> EntityPropertyImpl<T> getByName(String name, Class<T> type) {
        return (EntityPropertyImpl<T>) getByName(name);
    }

    @Override
    public void registerDummy(String name, Class<?> type) {
        register(new DummyProperty<>(name, type));
    }

    public EntityPropertyImpl<?> getByName(String name) {
        return byName.get(name.toLowerCase());
    }
}
