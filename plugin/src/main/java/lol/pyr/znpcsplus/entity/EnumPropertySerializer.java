package lol.pyr.znpcsplus.entity;

public class EnumPropertySerializer<T extends Enum<T>> implements PropertySerializer<T> {

    private final Class<T> enumClass;

    public EnumPropertySerializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String serialize(T property) {
        return property.name();
    }

    @Override
    public T deserialize(String property) {
        try {
            return Enum.valueOf(enumClass, property.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Class<T> getTypeClass() {
        return enumClass;
    }
}
