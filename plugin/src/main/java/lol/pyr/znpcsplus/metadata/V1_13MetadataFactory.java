package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;

@Deprecated
public class V1_13MetadataFactory extends V1_12MetadataFactory {

    @Override
    public EntityData usingItem(boolean usingItem, boolean offHand, boolean riptide) {
        return newEntityData(6, EntityDataTypes.BYTE, (byte) ((usingItem ? 0x01 : 0) | (offHand ? 0x02 : 0) | (riptide ? 0x04 : 0)));
    }
}
