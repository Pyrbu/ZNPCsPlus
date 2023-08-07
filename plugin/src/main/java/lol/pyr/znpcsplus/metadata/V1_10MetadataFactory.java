package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.util.CreeperState;

@Deprecated
public class V1_10MetadataFactory extends V1_9MetadataFactory {

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(12, EntityDataTypes.BYTE, (byte) (onFire ? 0x01 : 0));
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(12, EntityDataTypes.INT, state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(13, EntityDataTypes.BOOLEAN, charged);
    }

    @Override
    public EntityData villagerData(int type, int profession, int level) {
        return newEntityData(13, EntityDataTypes.INT, profession);
    }
}
