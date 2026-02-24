package lol.pyr.znpcsplus.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NpcLocationTest {
  @Test
  void centeredAdjustsXAndZToBlockCenter() {
    NpcLocation location = new NpcLocation(10.2, 64.0, -3.8f, 45.0f, 30.0f);

    NpcLocation centered = location.centered();

    assertEquals(10.5, centered.getX(), 0.00001);
    assertEquals(-3.5, centered.getZ(), 0.00001);
    assertEquals(64.0, centered.getY(), 0.00001);
    assertEquals(45.0f, centered.getYaw(), 0.00001);
    assertEquals(30.0f, centered.getPitch(), 0.00001);
  }

  @Test
  void lookingAtComputesYawAndPitch() {
    NpcLocation source = new NpcLocation(0.0, 0.0, 0.0, 0.0f, 0.0f);
    NpcLocation target = new NpcLocation(1.0, 0.0, 0.0, 0.0f, 0.0f);

    NpcLocation looked = source.lookingAt(target);

    assertEquals(270.0f, looked.getYaw(), 0.00001);
    assertEquals(0.0f, looked.getPitch(), 0.00001);
  }

  @Test
  void lookingAtVerticalTargetUsesNinetyDegreePitch() {
    NpcLocation source = new NpcLocation(1.0, 1.0, 1.0, 15.0f, 25.0f);
    NpcLocation target = new NpcLocation(1.0, 5.0, 1.0, 0.0f, 0.0f);

    NpcLocation looked = source.lookingAt(target);

    assertEquals(-90.0f, looked.getPitch(), 0.00001);
    assertEquals(source.getX(), looked.getX(), 0.00001);
    assertEquals(source.getY(), looked.getY(), 0.00001);
    assertEquals(source.getZ(), looked.getZ(), 0.00001);
  }
}
