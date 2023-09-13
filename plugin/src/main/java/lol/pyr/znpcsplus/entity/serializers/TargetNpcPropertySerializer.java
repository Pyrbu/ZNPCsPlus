package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;

public class TargetNpcPropertySerializer implements PropertySerializer<NpcEntryImpl> {
    @Override
    public String serialize(NpcEntryImpl property) {
        return property.getId();
    }

    @Override
    public NpcEntryImpl deserialize(String property) {
        return null; // TODO: find a way to do this
    }

    @Override
    public Class<NpcEntryImpl> getTypeClass() {
        return NpcEntryImpl.class;
    }
}
