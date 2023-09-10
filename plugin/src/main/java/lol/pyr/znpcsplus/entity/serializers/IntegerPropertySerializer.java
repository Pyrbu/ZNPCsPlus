package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;

public class IntegerPropertySerializer implements PropertySerializer<Integer> {
    @Override
    public String serialize(Integer property) {
        return String.valueOf(property);
    }

    @Override
    public Integer deserialize(String property) {
        int i = 0;
        try {
            i = Integer.parseInt(property);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Override
    public Class<Integer> getTypeClass() {
        return Integer.class;
    }
}
