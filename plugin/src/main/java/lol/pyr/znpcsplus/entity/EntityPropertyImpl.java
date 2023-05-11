package lol.pyr.znpcsplus.entity;

import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;
import java.util.Map;

public class EntityPropertyImpl<T> implements EntityProperty<T> {
    private final String name;
    private final T defaultValue;
    private final Class<T> clazz;

    private final PropertySerializer<T> serializer;
    private final PropertyDeserializer<T> deserializer;

    public EntityPropertyImpl(String name, Class<T> type, PropertySerializer<T> serializer, PropertyDeserializer<T> deserializer) {
        this(name, null, type, serializer, deserializer);
    }

    @SuppressWarnings("unchecked")
    public EntityPropertyImpl(String name, T defaultValue, PropertySerializer<T> serializer, PropertyDeserializer<T> deserializer) {
        this(name, defaultValue, (Class<T>) defaultValue.getClass(), serializer, deserializer);
    }

    private EntityPropertyImpl(String name, T defaultValue, Class<T> clazz, PropertySerializer<T> serializer, PropertyDeserializer<T> deserializer) {
        this.name = name.toUpperCase();
        this.defaultValue = defaultValue;
        this.clazz = clazz;
        this.serializer = serializer;
        this.deserializer = deserializer;
        BY_NAME.put(this.name, this);
    }

    public String getName() {
        return name;
    }

    public String serialize(PropertyHolder holder) {
        return serialize(holder.getProperty(this));
    }

    public String serialize(T value) {
        return serializer.serialize(value);
    }

    public T deserialize(String str) {
        return deserializer.deserialize(str);
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    public Class<T> getType() {
        return clazz;
    }

    private final static Map<String, EntityPropertyImpl<?>> BY_NAME = new HashMap<>();

    public static EntityPropertyImpl<?> getByName(String name) {
        return BY_NAME.get(name.toUpperCase());
    }

    @FunctionalInterface
    private interface PropertySerializer<T> {
        String serialize(T property);
    }

    @FunctionalInterface
    private interface PropertyDeserializer<T> {
        T deserialize(String property);
    }


    private final static PropertySerializer<Boolean> BOOLEAN_SERIALIZER = Object::toString;
    private final static PropertyDeserializer<Boolean> BOOLEAN_DESERIALIZER = Boolean::valueOf;

    private final static PropertySerializer<NamedTextColor> COLOR_SERIALIZER = color -> String.valueOf(color.value());
    private final static PropertyDeserializer<NamedTextColor> COLOR_DESERIALIZER = str -> NamedTextColor.namedColor(Integer.parseInt(str));

    private final static PropertySerializer<Component> COMPONENT_SERIALIZER = component -> MiniMessage.miniMessage().serialize(component);
    private final static PropertyDeserializer<Component> COMPONENT_DESERIALIZER = str -> MiniMessage.miniMessage().deserialize(str);

    private final static PropertySerializer<SkinDescriptor> DESCRIPTOR_SERIALIZER = descriptor -> ((BaseSkinDescriptor) descriptor).serialize();
    private final static PropertyDeserializer<SkinDescriptor> DESCRIPTOR_DESERIALIZER = BaseSkinDescriptor::deserialize;

    public static EntityPropertyImpl<Boolean> SKIN_LAYERS = new EntityPropertyImpl<>("skin_layers", true, BOOLEAN_SERIALIZER, BOOLEAN_DESERIALIZER);
    public static EntityPropertyImpl<SkinDescriptor> SKIN = new EntityPropertyImpl<>("skin", SkinDescriptor.class, DESCRIPTOR_SERIALIZER, DESCRIPTOR_DESERIALIZER);
    public static EntityPropertyImpl<NamedTextColor> GLOW = new EntityPropertyImpl<>("glow", NamedTextColor.class, COLOR_SERIALIZER, COLOR_DESERIALIZER);
    public static EntityPropertyImpl<Boolean> FIRE = new EntityPropertyImpl<>("fire", false, BOOLEAN_SERIALIZER, BOOLEAN_DESERIALIZER);
    public static EntityPropertyImpl<Boolean> INVISIBLE = new EntityPropertyImpl<>("invisible", false, BOOLEAN_SERIALIZER, BOOLEAN_DESERIALIZER);
    public static EntityPropertyImpl<Boolean> SILENT = new EntityPropertyImpl<>("silent", false, BOOLEAN_SERIALIZER, BOOLEAN_DESERIALIZER);
    public static EntityPropertyImpl<Component> NAME = new EntityPropertyImpl<>("name", Component.class, COMPONENT_SERIALIZER, COMPONENT_DESERIALIZER);
}