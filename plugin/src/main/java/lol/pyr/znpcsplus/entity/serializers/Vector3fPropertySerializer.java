package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.util.Vector3f;

public class Vector3fPropertySerializer implements PropertySerializer<Vector3f> {

    @Override
    public String serialize(Vector3f property) {
        return property.toString();
    }

    @Override
    public Vector3f deserialize(String property) {
        return new Vector3f(property);
    }

    @Override
    public Class<Vector3f> getTypeClass() {
        return Vector3f.class;
    }
}
