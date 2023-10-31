package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import net.kyori.adventure.text.format.NamedTextColor;

@Deprecated
public class NamedTextColorPropertySerializer implements PropertySerializer<NamedTextColor> {
    @Override
    public String serialize(NamedTextColor property) {
        return String.valueOf(property.value());
    }

    @Override
    public NamedTextColor deserialize(String property) {
        return NamedTextColor.namedColor(Integer.parseInt(property));
    }

    @Override
    public Class<NamedTextColor> getTypeClass() {
        return NamedTextColor.class;
    }
}
