package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.util.LookType;

public class LookTypeSerializer implements PropertySerializer<LookType> {
    @Override
    public String serialize(LookType property) {
        return property.name();
    }

    @Override
    public LookType deserialize(String property) {
        if (property.equals("true")) return LookType.CLOSEST_PLAYER;
        try {
             return LookType.valueOf(property);
        } catch (IllegalArgumentException ignored) {
            return LookType.FIXED;
        }
    }

    @Override
    public Class<LookType> getTypeClass() {
        return LookType.class;
    }
}
