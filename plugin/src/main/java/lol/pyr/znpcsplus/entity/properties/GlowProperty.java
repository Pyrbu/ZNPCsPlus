package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class GlowProperty extends EntityPropertyImpl<NamedTextColor> {
    private final PacketFactory packetFactory;

    public GlowProperty(PacketFactory packetFactory) {
        super("glow", null, NamedTextColor.class);
        this.packetFactory = packetFactory;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        NamedTextColor value = entity.getProperty(this);
        EntityData oldData = properties.get(0);
        byte oldValue = oldData == null ? 0 : (byte) oldData.getValue();
        properties.put(0, newEntityData(0, EntityDataTypes.BYTE, (byte) (oldValue | (value == null ? 0 : 0x40))));
        if (isSpawned) packetFactory.removeTeam(player, entity);
        packetFactory.createTeam(player, entity, value);
    }
}
