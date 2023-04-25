package lol.pyr.znpcsplus.entity;

import lol.pyr.znpcsplus.skin.SkinDescriptor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;

public class EntityProperty<T> {
    private final String name;
    private final T defaultValue;

    public EntityProperty(String name) {
        this(name, null);
    }

    public EntityProperty(String name, T defaultValue) {
        this.name = name.toUpperCase();
        this.defaultValue = defaultValue;
        BY_NAME.put(this.name, this);
    }

    public String name() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    private final static Map<String, EntityProperty<?>> BY_NAME = new HashMap<>();

    public static EntityProperty<?> getByName(String name) {
        return BY_NAME.get(name.toUpperCase());
    }

    public static EntityProperty<Boolean> SKIN_LAYERS = new EntityProperty<>("skin_layers", true);
    public static EntityProperty<SkinDescriptor> SKIN = new EntityProperty<>("skin");
    public static EntityProperty<NamedTextColor> GLOW = new EntityProperty<>("glow");
    public static EntityProperty<Boolean> FIRE = new EntityProperty<>("fire", false);
    public static EntityProperty<Boolean> INVISIBLE = new EntityProperty<>("invisible", false);
    public static EntityProperty<Boolean> SILENT = new EntityProperty<>("silent", false);
    public static EntityProperty<Component> NAME = new EntityProperty<>("name");
}