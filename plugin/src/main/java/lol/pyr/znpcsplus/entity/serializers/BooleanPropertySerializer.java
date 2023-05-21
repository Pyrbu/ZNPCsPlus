package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;

public class BooleanPropertySerializer implements PropertySerializer<Boolean> {
    @Override
    public String serialize(Boolean property) {
        return String.valueOf(property);
    }

    @Override
    public Boolean deserialize(String property) {
        return Boolean.valueOf(property);
    }

    @Override
    public Class<Boolean> getTypeClass() {
        return Boolean.class;
    }
}
