package lol.pyr.znpcsplus.entity.properties.villager;

import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfessions;
import lol.pyr.znpcsplus.util.VillagerProfession;

public class VillagerProfessionProperty extends VillagerDataProperty<VillagerProfession> {
    public VillagerProfessionProperty(String name, int index, VillagerProfession def) {
        super(name, index, def);
    }

    @Override
    protected VillagerData apply(VillagerData data, VillagerProfession value) {
        data.setProfession(VillagerProfessions.getById(value.getId()));
        return data;
    }
}
