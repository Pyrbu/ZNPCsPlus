package lol.pyr.znpcsplus.entity.properties.player;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import org.bukkit.entity.Player;

import java.util.Map;

public class AlwaysVisibleInTabProperty extends EntityPropertyImpl<Boolean> {
    private final PacketFactory packetFactory;

    public AlwaysVisibleInTabProperty(PacketFactory packetFactory) {
        super("always_visible_in_tab", false, Boolean.class);
        this.packetFactory = packetFactory;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData<?>> properties) {
        if (!isSpawned) return;
        packetFactory.updateListed(player, entity, entity.getProperty(this));
    }
}
