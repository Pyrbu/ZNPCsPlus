package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import org.bukkit.entity.Player;

import java.util.Map;

public class TargetNpcProperty extends EntityPropertyImpl<NpcEntryImpl> {
    private final int index;

    public TargetNpcProperty(String name, int index, NpcEntryImpl defaultValue) {
        super(name, defaultValue, NpcEntryImpl.class);
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        NpcEntryImpl value = entity.getProperty(this);
        if (value == null) return;
        if (value.getNpc().getEntity().getEntityId() == entity.getEntityId()) return;
        if (value.getNpc().isVisibleTo(player)) {
            properties.put(index, newEntityData(index, EntityDataTypes.INT, value.getNpc().getEntity().getEntityId()));
        }
    }
}
