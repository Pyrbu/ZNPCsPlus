package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.entity.serializers.*;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import lol.pyr.znpcsplus.util.CatVariant;
import lol.pyr.znpcsplus.util.CreeperState;
import lol.pyr.znpcsplus.util.NpcPose;
import lol.pyr.znpcsplus.util.Vector3f;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class EntityPropertyRegistryImpl implements EntityPropertyRegistry {
    private final Map<Class<?>, PropertySerializer<?>> serializerMap = new HashMap<>();
    private final Map<String, EntityPropertyImpl<?>> byName = new HashMap<>();

    public EntityPropertyRegistryImpl(SkinCache skinCache) {
        registerSerializer(new BooleanPropertySerializer());
        registerSerializer(new ComponentPropertySerializer());
        registerSerializer(new NamedTextColorPropertySerializer());
        registerSerializer(new SkinDescriptorSerializer(skinCache));
        registerSerializer(new ItemStackPropertySerializer());
        registerSerializer(new ColorPropertySerializer());
        registerSerializer(new Vector3fPropertySerializer());

        registerEnumSerializer(NpcPose.class);
        registerEnumSerializer(DyeColor.class);
        registerEnumSerializer(CatVariant.class);
        registerEnumSerializer(CreeperState.class);

        registerType("glow", NamedTextColor.class);
        registerType("fire", false);
        registerType("invisible", false);
        registerType("silent", false);
        registerType("skin", SkinDescriptor.class);
        registerType("name", Component.class);
        registerType("look", false);

        // TODO: make all of these bukkit itemstack classes so api users wont have to add packetevents as a dependency
        registerType("helmet", ItemStack.class);
        registerType("chestplate", ItemStack.class);
        registerType("leggings", ItemStack.class);
        registerType("boots", ItemStack.class);
        registerType("hand", ItemStack.class);
        registerType("offhand", ItemStack.class);

        registerType("using_item", false); // TODO: fix it for 1.8 and add new property to use offhand item and riptide animation
        registerType("potion_color", Color.BLACK);
        registerType("potion_ambient", false);
        registerType("shaking", false);
        registerType("baby", false); // TODO
        registerType("pose", NpcPose.STANDING);

        // Player
        registerType("skin_cape", true);
        registerType("skin_jacket", true);
        registerType("skin_left_sleeve", true);
        registerType("skin_right_sleeve", true);
        registerType("skin_left_leg", true);
        registerType("skin_right_leg", true);
        registerType("skin_hat", true);

        // End Crystal
        registerType("beam_target", null); // TODO: Make a block pos class for this
        registerType("show_base", true); // TODO

        // Armor Stand
        registerType("small", false);
        registerType("arms", false);
        registerType("base_plate", true);

        registerType("head_rotation", Vector3f.zero());
        registerType("body_rotation", Vector3f.zero());
        registerType("left_arm_rotation", new Vector3f(-10, 0, -10));
        registerType("right_arm_rotation", new Vector3f(-15, 0, 10));
        registerType("left_leg_rotation", new Vector3f(-1 , 0, -1));
        registerType("right_leg_rotation", new Vector3f(1, 0, 1));

        // Axolotl
        registerType("axolotl_variant", 0);
        registerType("playing_dead", false); // TODO fix disabling

        // Bat
        registerType("hanging", false);

        // Bee
        registerType("angry", false);
        registerType("has_nectar", false);

        // Blaze
        registerType("blaze_on_fire", false);

        // Cat
        registerType("cat_variant", CatVariant.BLACK);
        registerType("cat_lying", false);
        registerType("cat_collar_color", DyeColor.RED);

        // Creeper
        registerType("creeper_state", CreeperState.IDLE); // TODO: -1 = idle, 1 = fuse
        registerType("creeper_charged", false); // TODO

        // Pufferfish
        registerType("puff_state", null); // TODO: Make a puff state enum class

        // Tropical Fish
        registerType("tropical_fish_variant", null); // TODO: Maybe make an enum class for this? its just an int on wiki.vg

        // Sniffer
        registerType("sniffer_state", null); // TODO: Nothing on wiki.vg, look in mc source

        // Horse
        registerType("horse_style", 0); // TODO: Figure this out
        registerType("horse_chest", false); // TODO
        registerType("horse_saddle", false); // TODO

        // LLama
        registerType("carpet_color", DyeColor.class); // TODO
        registerType("llama_variant", 0); // TODO

        // Fox
        registerType("fox_variant", 0); // TODO: 0 = red, 1 = snow
        registerType("fox_sitting", false); // TODO
        registerType("fox_crouching", false); // TODO
        registerType("fox_sleeping", false); // TODO
        registerType("fox_faceplanting", false); // TODO

        // Frog
        registerType("frog_type", null); // TODO: It has a custom type read on wiki.vg

        // Panda
        registerType("panda_sneezing", false); // TODO
        registerType("panda_rolling", false); // TODO
        registerType("panda_sitting", false); // TODO
        registerType("panda_on_back", false); // TODO

        // Pig
        registerType("pig_saddle", false); // TODO

        // Rabbit
        registerType("rabbit_type", 0); // TODO: Figure this out

        // Polar Bear
        registerType("polar_bear_standing", false); // TODO

        // Sheep
        registerType("sheep_color", DyeColor.WHITE); // TODO: Figure this out
        registerType("sheep_sheared", false); // TODO

        // Strider
        registerType("strider_shaking", false); // TODO
        registerType("strider_saddle", false); // TODO

        // Wolf
        registerType("wolf_collar_color", DyeColor.RED); // TODO
        registerType("wolf_angry", false); // TODO

        // Parrot
        registerType("parrot_variant", 0); // TODO

        // Villager
        registerType("villager_ethnicity", 1); // TODO: how tf does this work? probably need to look in mc src
        registerType("villager_job", null); // TODO

        // Show Golem
        registerType("pumpkin", true); // TODO

        // Shulker
        registerType("attach_direction", null); // TODO: make a direction enum
        registerType("shield_height", 0); // TODO: figure this out
        registerType("shulker_color", DyeColor.RED); // TODO

        // Piglin / Hoglin
        registerType("immune_to_zombification", false); // TODO

        // Piglin
        registerType("piglin_dancing", false); // TODO
        registerType("piglin_charging_crossbow", false); // TODO

        // Goat
        registerType("has_left_horn", true); // TODO
        registerType("has_right_horn", true); // TODO

        // Vindicator
        registerType("celebrating", false); // TODO

        // Wither
        registerType("invulnerable_time", 0); // TODO

        // Enderman
        registerType("enderman_held_block", null); // TODO: figure out the type on this
        registerType("enderman_screaming", false); // TODO

        // Ghast
        registerType("attacking", false); // TODO

        // Phantom
        registerType("phantom_size", 0); // TODO

        // Slime
        registerType("slime_size", 0); // TODO
    }

    private void registerSerializer(PropertySerializer<?> serializer) {
        serializerMap.put(serializer.getTypeClass(), serializer);
    }

    private <T extends Enum<T>> void registerEnumSerializer(Class<T> clazz) {
        serializerMap.put(clazz, new EnumPropertySerializer<>(clazz));
    }

    private <T> void registerType(String name, Class<T> type) {
        registerType(name, null, type);
    }

    private <T> void registerType(String name, T defaultValue) {
        registerType(name, defaultValue, (Class<T>) defaultValue.getClass());
    }

    private <T> void registerType(String name, T defaultValue, Class<T> clazz) {
        if (clazz == null) return;
        EntityPropertyImpl<T> property = new EntityPropertyImpl<>(name, defaultValue, clazz, (PropertySerializer<T>) serializerMap.get(clazz));
        byName.put(name.toLowerCase(), property);
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

    public EntityPropertyImpl<?> getByName(String name) {
        return byName.get(name.toLowerCase());
    }
}
