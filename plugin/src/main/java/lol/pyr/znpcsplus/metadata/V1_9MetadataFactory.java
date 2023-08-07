package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.util.CreeperState;

@Deprecated
public class V1_9MetadataFactory extends V1_8MetadataFactory {

    @Override
    public EntityData blazeOnFire(boolean onFire) {
        return newEntityData(16, EntityDataTypes.BYTE, (byte) (onFire ? 1 : 0));
    }

    @Override
    public EntityData creeperState(CreeperState state) {
        return newEntityData(11, EntityDataTypes.INT, state.getState());
    }

    @Override
    public EntityData creeperCharged(boolean charged) {
        return newEntityData(12, EntityDataTypes.BOOLEAN, charged);
    }

    @Override
    public EntityData villagerData(int type, int profession, int level) {
        return newEntityData(12, EntityDataTypes.INT, profession);
    }
}
