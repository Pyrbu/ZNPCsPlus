package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.util.NamedColor;

public class NamedColorPropertySerializer implements PropertySerializer<NamedColor> {
    @Override
    public String serialize(NamedColor property) {
        return property.name();
    }

    @Override
    public NamedColor deserialize(String property) {
        try {
            return NamedColor.valueOf(property.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return NamedColor.WHITE;
        }
    }

    @Override
    public Class<NamedColor> getTypeClass() {
        return NamedColor.class;
    }
}