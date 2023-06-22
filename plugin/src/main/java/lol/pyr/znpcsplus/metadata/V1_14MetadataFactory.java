package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;

public class V1_14MetadataFactory extends V1_13MetadataFactory {
    @Override
    public EntityData skinLayers(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat) {
        return createSkinLayers(15, cape, jacket, leftSleeve, rightSleeve, leftLeg, rightLeg, hat);
    }

    @Override
    public EntityData pose(EntityPose pose) {
        return newEntityData(6, EntityDataTypes.ENTITY_POSE, pose);
    }
}
