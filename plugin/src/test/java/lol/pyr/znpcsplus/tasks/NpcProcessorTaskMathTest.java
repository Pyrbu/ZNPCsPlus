package lol.pyr.znpcsplus.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;

public class NpcProcessorTaskMathTest {
  @Test
  void distanceSquaredCalculatesEuclideanDistance() {
    NpcLocation first = new NpcLocation(0.0, 0.0, 0.0, 0.0f, 0.0f);
    NpcLocation second = new NpcLocation(3.0, 4.0, 12.0, 0.0f, 0.0f);

    double distanceSquared = NpcProcessorTask.distanceSquared(first, second);

    assertEquals(169.0, distanceSquared, 0.000001);
  }

  @Test
  void knockbackVectorPushesAwayOnXAxis() {
    NpcLocation npc = new NpcLocation(0.0, 0.0, 0.0, 0.0f, 0.0f);
    NpcLocation player = new NpcLocation(1.0, 0.0, 0.0, 0.0f, 0.0f);

    Vector vector = NpcProcessorTask.calculateKnockbackVector(npc, player, 1.5, 0.35);

    assertEquals(1.5, vector.getX(), 0.000001);
    assertEquals(0.35, vector.getY(), 0.000001);
    assertEquals(0.0, vector.getZ(), 0.000001);
  }

  @Test
  void knockbackVectorPushesAwayOnZAxis() {
    NpcLocation npc = new NpcLocation(0.0, 0.0, 0.0, 0.0f, 0.0f);
    NpcLocation player = new NpcLocation(0.0, 0.0, -2.0, 0.0f, 0.0f);

    Vector vector = NpcProcessorTask.calculateKnockbackVector(npc, player, 2.0, 0.15);

    assertEquals(0.0, vector.getX(), 0.000001);
    assertEquals(0.15, vector.getY(), 0.000001);
    assertEquals(-2.0, vector.getZ(), 0.000001);
  }
}
