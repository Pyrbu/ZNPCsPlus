package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.util.GlowColor;
import net.kyori.adventure.text.format.NamedTextColor;

public class GlowColorPropertySerializer implements PropertySerializer<GlowColor> {
    @Override
    public String serialize(GlowColor property) {
        return String.valueOf(property.value());
    }

    @Override
    public GlowColor deserialize(String property) {
        return GlowColor.namedColor(Integer.parseInt(property));
    }

    @Override
    public Class<GlowColor> getTypeClass() {
        return GlowColor.class;
    }
}