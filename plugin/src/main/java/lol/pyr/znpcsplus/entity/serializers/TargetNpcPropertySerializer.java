package lol.pyr.znpcsplus.entity.serializers;

import java.util.Objects;
import java.util.function.Function;
import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;

public class TargetNpcPropertySerializer implements PropertySerializer<NpcEntryImpl> {
  private static Function<String, NpcEntryImpl> idResolver = id -> null;

  public static void setIdResolver(Function<String, NpcEntryImpl> resolver) {
    idResolver = Objects.requireNonNull(resolver, "resolver");
  }

  @Override
  public String serialize(NpcEntryImpl property) {
    return property.getId();
  }

  @Override
  public NpcEntryImpl deserialize(String property) {
    if (property == null || property.isEmpty()) return null;
    return idResolver.apply(property.toLowerCase());
  }

  @Override
  public Class<NpcEntryImpl> getTypeClass() {
    return NpcEntryImpl.class;
  }
}
