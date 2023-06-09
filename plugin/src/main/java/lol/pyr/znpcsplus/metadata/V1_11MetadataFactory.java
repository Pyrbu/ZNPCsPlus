package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;

public class V1_11MetadataFactory extends V1_10MetadataFactory {
    @Override
    public EntityData effects(boolean onFire, boolean glowing, boolean invisible, boolean usingElytra, boolean usingItemLegacy) {
        return super.effects(onFire, glowing, invisible, usingElytra, false);
    }

   @Override
    public EntityData usingItem(boolean usingItem, boolean offHand, boolean riptide) {
        return newEntityData(6, EntityDataTypes.BYTE, (byte) ((usingItem ? 0x01 : 0) | (offHand ? 0x02 : 0)));
    }

    @Override
    public EntityData evokerSpell(int spell) {
        return newEntityData(12, EntityDataTypes.BYTE, (byte) spell);
    }
}
