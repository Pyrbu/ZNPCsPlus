package lol.pyr.znpcsplus.entity.properties.villager;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfessions;
import com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class VillagerDataProperty<T> extends EntityPropertyImpl<T> {
    private final int index;

    @SuppressWarnings("unchecked")
    public VillagerDataProperty(String name, int index, T def) {
        super(name, def, (Class<T>) def.getClass());
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        EntityData oldData = properties.get(index);
        VillagerData old = oldData == null ? new VillagerData(VillagerTypes.PLAINS, VillagerProfessions.NONE, 1) : (VillagerData) oldData.getValue();
        properties.put(index, newEntityData(index, EntityDataTypes.VILLAGER_DATA, apply(old, entity.getProperty(this))));
    }

    protected abstract VillagerData apply(VillagerData data, T value);
}