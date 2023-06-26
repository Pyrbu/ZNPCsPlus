package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import org.bukkit.Color;

public class ColorPropertySerializer implements PropertySerializer<Color> {
    @Override
    public String serialize(Color property) {
        return String.valueOf(property.asRGB());
    }

    @Override
    public Color deserialize(String property) {
        return Color.fromRGB(Integer.parseInt(property));
    }

    @Override
    public Class<Color> getTypeClass() {
        return Color.class;
    }
}
