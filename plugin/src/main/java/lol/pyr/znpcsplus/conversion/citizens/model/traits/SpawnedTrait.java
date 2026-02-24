package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import lol.pyr.znpcsplus.conversion.citizens.model.CitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import org.jetbrains.annotations.NotNull;

public class SpawnedTrait extends CitizensTrait {

  public SpawnedTrait() {
    super("spawned");
  }

  @Override
  public @NotNull NpcImpl apply(NpcImpl npc, Object value) {
    if (value != null) {
      npc.setEnabled((boolean) value);
    }
    return npc;
  }
}
