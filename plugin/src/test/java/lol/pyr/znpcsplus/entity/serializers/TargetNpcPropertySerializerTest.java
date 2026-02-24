package lol.pyr.znpcsplus.entity.serializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class TargetNpcPropertySerializerTest {
  @Test
  void deserializeNormalizesIdToLowercase() {
    AtomicReference<String> capturedId = new AtomicReference<>();
    TargetNpcPropertySerializer.setIdResolver(
        id -> {
          capturedId.set(id);
          return null;
        });

    TargetNpcPropertySerializer serializer = new TargetNpcPropertySerializer();
    serializer.deserialize("TeSt_NpC");

    assertEquals("test_npc", capturedId.get());
  }

  @Test
  void deserializeReturnsNullForBlankValue() {
    TargetNpcPropertySerializer.setIdResolver(
        id -> {
          throw new AssertionError("Resolver must not be called for blank values");
        });

    TargetNpcPropertySerializer serializer = new TargetNpcPropertySerializer();

    assertNull(serializer.deserialize(""));
    assertNull(serializer.deserialize(null));
  }
}
