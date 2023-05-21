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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class EntityPropertyRegistryImpl implements EntityPropertyRegistry {
    private final Map<Class<?>, PropertySerializer<?>> serializerMap = new HashMap<>();
    private final List<EntityPropertyImpl<?>> properties = new ArrayList<>();

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
    }

    private void registerSerializer(PropertySerializer<?> serializer) {
        serializerMap.put(serializer.getTypeClass(), serializer);
    }

    private <T> void registerType(String name, Class<T> type) {
        properties.add(new EntityPropertyImpl<>(name, type, (PropertySerializer<T>) serializerMap.get(type)));
    }

    private <T> void registerType(String name, T defaultValue) {
        properties.add(new EntityPropertyImpl<>(name, defaultValue, (PropertySerializer<T>) serializerMap.get(defaultValue.getClass())));
    }

    public <T> EntityPropertyImpl<T> getByName(String name, Class<T> type) {
        return (EntityPropertyImpl<T>) getByName(name);
    }

    public EntityPropertyImpl<?> getByName(String name) {
        for (EntityPropertyImpl<?> property : properties) if (property.getName().equalsIgnoreCase(name)) return property;
        return null;
    }
}
