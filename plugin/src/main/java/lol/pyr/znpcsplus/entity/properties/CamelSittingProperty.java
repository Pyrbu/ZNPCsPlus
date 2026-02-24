package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import java.util.Map;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

public class CamelSittingProperty extends EntityPropertyImpl<Boolean> {
  private final int poseIndex;
  private final int lastPoseTickIndex;

  public CamelSittingProperty(int poseIndex, int lastPoseTickIndex) {
    super("camel_sitting", false, Boolean.class);
    this.poseIndex = poseIndex;
    this.lastPoseTickIndex = lastPoseTickIndex;
  }

  @Override
  public void apply(
      Player player,
      PacketEntity entity,
      boolean isSpawned,
      Map<Integer, EntityData<?>> properties) {
    boolean value = entity.getProperty(this);
    if (value) {
      properties.put(
          poseIndex, newEntityData(poseIndex, EntityDataTypes.ENTITY_POSE, EntityPose.SITTING));
      properties.put(
          lastPoseTickIndex, newEntityData(lastPoseTickIndex, EntityDataTypes.LONG, -1L));
    } else {
      properties.put(
          poseIndex, newEntityData(poseIndex, EntityDataTypes.ENTITY_POSE, EntityPose.STANDING));
      properties.put(lastPoseTickIndex, newEntityData(lastPoseTickIndex, EntityDataTypes.LONG, 0L));
    }
  }
}
