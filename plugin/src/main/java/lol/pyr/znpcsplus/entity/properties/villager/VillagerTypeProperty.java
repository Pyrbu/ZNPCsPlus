package lol.pyr.znpcsplus.entity.properties.villager;

import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;
import lol.pyr.znpcsplus.util.VillagerType;

public class VillagerTypeProperty extends VillagerDataProperty<VillagerType> {
    public VillagerTypeProperty(String name, int index, VillagerType def) {
        super(name, index, def);
    }

    @Override
    protected VillagerData apply(VillagerData data, VillagerType value) {
        data.setType(VillagerTypes.getById(value.getId()));
        return data;
    }
}
