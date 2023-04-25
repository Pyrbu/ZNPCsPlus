package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.skin.SkinDescriptor;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;

public class NPCProperty<T> {
    private final String name;
    private final T defaultValue;

    public NPCProperty(String name) {
        this(name, null);
    }

    public NPCProperty(String name, T defaultValue) {
        this.name = name.toUpperCase();
        this.defaultValue = defaultValue;
        BY_NAME.put(this.name, this);
    }

    public String name() {
        return name;
    }

    protected T getDefaultValue() {
        return defaultValue;
    }

    private final static Map<String, NPCProperty<?>> BY_NAME = new HashMap<>();

    public static NPCProperty<?> getByName(String name) {
        return BY_NAME.get(name.toUpperCase());
    }

    public static NPCProperty<Boolean> SKIN_LAYERS = new NPCProperty<>("skin_layers", true);
    public static NPCProperty<SkinDescriptor> SKIN = new NPCProperty<>("skin");
    public static NPCProperty<NamedTextColor> GLOW = new NPCProperty<>("glow");
    public static NPCProperty<Boolean> FIRE = new NPCProperty<>("fire", false);
    public static NPCProperty<Boolean> INVISIBLE = new NPCProperty<>("invisible", false);
}