package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.SnifferState;
import org.bukkit.entity.Player;

import java.util.Map;

public class SnifferStateProperty extends EntityPropertyImpl<SnifferState> {
    private final int index;

    public SnifferStateProperty(int index) {
        super("sniffer_state", SnifferState.IDLING, SnifferState.class);
        this.index = index;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        SnifferState state = entity.getProperty(this);
        if (state == null) return;
        try {
            properties.put(index, newEntityData(index, EntityDataTypes.SNIFFER_STATE, com.github.retrooper.packetevents.protocol.entity.sniffer.SnifferState.valueOf(state.name())));
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }
}
