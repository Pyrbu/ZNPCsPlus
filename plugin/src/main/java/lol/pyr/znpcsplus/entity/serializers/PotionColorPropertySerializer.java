package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.util.PotionColor;
import lol.pyr.znpcsplus.entity.PropertySerializer;

public class PotionColorPropertySerializer implements PropertySerializer<PotionColor> {
    @Override
    public String serialize(PotionColor property) {
        return property.toString();
    }

    @Override
    public PotionColor deserialize(String property) {
        return new PotionColor(property);
    }

    @Override
    public Class<PotionColor> getTypeClass() {
        return PotionColor.class;
    }
}
