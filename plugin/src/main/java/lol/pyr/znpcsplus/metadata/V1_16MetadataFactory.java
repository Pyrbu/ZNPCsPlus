package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;

@Deprecated
public class V1_16MetadataFactory extends V1_15MetadataFactory {
    @Override
    public EntityData skinLayers(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat) {
        return createSkinLayers(16, cape, jacket, leftSleeve, rightSleeve, leftLeg, rightLeg, hat);
    }

    @Override
    public EntityData hoglinImmuneToZombification(boolean immuneToZombification) {
        return newEntityData(16, EntityDataTypes.BOOLEAN, immuneToZombification);
    }
}
