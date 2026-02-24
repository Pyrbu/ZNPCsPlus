package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import java.util.Map;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import org.bukkit.entity.Player;

public class ForceBodyRotationProperty extends DummyProperty<Boolean> {
  private final TaskScheduler scheduler;

  public ForceBodyRotationProperty(TaskScheduler scheduler) {
    super("force_body_rotation", false);
    this.scheduler = scheduler;
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    if (entity.getProperty(this)) {
      scheduler.runLaterAsync(() -> entity.swingHand(player, false), 2L);
      scheduler.runLaterAsync(() -> entity.swingHand(player, false), 6L);
    }
  }
}
