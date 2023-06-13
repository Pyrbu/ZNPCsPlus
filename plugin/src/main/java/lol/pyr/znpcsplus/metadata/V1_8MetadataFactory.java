package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import lol.pyr.znpcsplus.util.list.ListUtil;
import net.kyori.adventure.text.Component;

import java.util.Collection;

public class V1_8MetadataFactory implements MetadataFactory {
    @Override
    public EntityData skinLayers(boolean enabled) {
        return createSkinLayers(12, enabled);
    }

    @Override
    public EntityData cape(boolean enabled) {
        return createCape(10, enabled);
    }

    @Override
    public EntityData effects(boolean onFire, boolean glowing, boolean invisible) {
        return newEntityData(0, EntityDataTypes.BYTE, (byte) ((onFire ? 0x01 : 0) | (invisible ? 0x20 : 0)));
    }

    @Override
    public Collection<EntityData> name(Component name) {
        return ListUtil.immutableList(
                newEntityData(2, EntityDataTypes.STRING, AdventureSerializer.getGsonSerializer().serialize(name)),
                newEntityData(3, EntityDataTypes.BYTE, (byte) 1)
        );
    }

    @Override
    public EntityData noGravity() {
        throw new UnsupportedOperationException("The gravity entity data isn't supported on this version");
    }

    @Override
    public EntityData silent(boolean enabled) {
        return newEntityData(4, EntityDataTypes.BYTE, (byte) (enabled ? 1 : 0));
    }

    protected EntityData createSkinLayers(int index, boolean enabled) {
        return newEntityData(index, EntityDataTypes.BYTE, enabled ? Byte.MAX_VALUE : (byte) 0);
    }

    protected EntityData createCape(int index, boolean enabled) {
        return newEntityData(index, EntityDataTypes.BYTE, (byte) (enabled ? 1 : 0));
    }

    protected <T> EntityData newEntityData(int index, EntityDataType<T> type, T value) {
        return new EntityData(index, type, value);
    }
}
