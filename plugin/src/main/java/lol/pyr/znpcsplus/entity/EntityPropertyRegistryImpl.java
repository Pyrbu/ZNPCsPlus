package lol.pyr.znpcsplus.entity;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.entity.serializers.BooleanPropertySerializer;
import lol.pyr.znpcsplus.entity.serializers.ComponentPropertySerializer;
import lol.pyr.znpcsplus.entity.serializers.NamedTextColorPropertySerializer;
import lol.pyr.znpcsplus.entity.serializers.SkinDescriptorSerializer;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class EntityPropertyRegistryImpl implements EntityPropertyRegistry {
    private final Map<Class<?>, PropertySerializer<?>> serializerMap = new HashMap<>();
    private final Map<String, EntityPropertyImpl<?>> byName = new HashMap<>();

    public EntityPropertyRegistryImpl(SkinCache skinCache) {
        registerSerializer(new BooleanPropertySerializer());
        registerSerializer(new ComponentPropertySerializer());
        registerSerializer(new NamedTextColorPropertySerializer());
        registerSerializer(new SkinDescriptorSerializer(skinCache));

        registerType("glow", NamedTextColor.class);
        registerType("skin_layers", true);
        registerType("fire", false);
        registerType("invisible", false);
        registerType("silent", false);
        registerType("skin", SkinDescriptor.class);
        registerType("name", Component.class);
        registerType("look", false);
    }

    private void registerSerializer(PropertySerializer<?> serializer) {
        serializerMap.put(serializer.getTypeClass(), serializer);
    }

    private <T> void registerType(String name, Class<T> type) {
        registerType(name, null, type);
    }

    private <T> void registerType(String name, T defaultValue) {
        registerType(name, defaultValue, (Class<T>) defaultValue.getClass());
    }

    private <T> void registerType(String name, T defaultValue, Class<T> clazz) {
        EntityPropertyImpl<T> property = new EntityPropertyImpl<>(name, defaultValue, clazz, (PropertySerializer<T>) serializerMap.get(clazz));
        byName.put(name.toLowerCase(), property);
    }

    public <T> EntityPropertyImpl<T> getByName(String name, Class<T> type) {
        return (EntityPropertyImpl<T>) getByName(name);
    }

    public EntityPropertyImpl<?> getByName(String name) {
        return byName.get(name.toLowerCase());
    }
}
