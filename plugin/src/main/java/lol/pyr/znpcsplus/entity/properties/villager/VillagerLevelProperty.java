package lol.pyr.znpcsplus.entity.properties.villager;

import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import lol.pyr.znpcsplus.util.VillagerLevel;

public class VillagerLevelProperty extends VillagerDataProperty<VillagerLevel> {
    public VillagerLevelProperty(String name, int index, VillagerLevel def) {
        super(name, index, def);
    }

    @Override
    protected VillagerData apply(VillagerData data, VillagerLevel value) {
        data.setLevel(value.ordinal() + 1);
        return data;
    }
}
