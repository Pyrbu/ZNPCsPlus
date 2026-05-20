package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class StriderShakingProperty extends EntityPropertyImpl<Boolean> {
    private final int striderIndex;
    private final int noAIIndex;
    private final int noAIBitmask;

    public StriderShakingProperty(int striderIndex, int noAIIndex, int noAIBitmask) {
        super("strider_shaking", true, Boolean.class);
        this.striderIndex = striderIndex;
        this.noAIIndex = noAIIndex;
        this.noAIBitmask = noAIBitmask;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData<?>> properties) {
        boolean enabled = entity.getProperty(this);
        properties.put(striderIndex, newEntityData(striderIndex, EntityDataTypes.BOOLEAN, enabled));
        properties.put(noAIIndex, newEntityData(noAIIndex, EntityDataTypes.BYTE, (byte) (enabled ? 0 : noAIBitmask)));
    }
}
