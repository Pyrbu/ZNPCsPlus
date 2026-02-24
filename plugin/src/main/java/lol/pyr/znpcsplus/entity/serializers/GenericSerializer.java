package lol.pyr.znpcsplus.entity.serializers;

import java.util.function.Function;
import lol.pyr.znpcsplus.entity.PropertySerializer;

public class GenericSerializer<T> implements PropertySerializer<T> {
  private final Function<T, String> encoder;
  private final Function<String, T> decoder;
  private final Class<T> typeClass;

  public GenericSerializer(
      Function<T, String> encoder, Function<String, T> decoder, Class<T> typeClass) {
    this.encoder = encoder;
    this.decoder = decoder;
    this.typeClass = typeClass;
  }

  @Override
  public String serialize(T property) {
    return encoder.apply(property);
  }

  @Override
  public T deserialize(String property) {
    return decoder.apply(property);
  }

  @Override
  public Class<T> getTypeClass() {
    return typeClass;
  }
}
