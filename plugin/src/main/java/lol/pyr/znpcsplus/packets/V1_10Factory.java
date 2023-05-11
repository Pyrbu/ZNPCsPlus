package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import org.bukkit.entity.Player;

import java.util.Map;

public class V1_10Factory extends V1_9Factory {
    @Override
    public Map<Integer, EntityData> generateMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        Map<Integer, EntityData> data = super.generateMetadata(player, entity, properties);
        add(data, MetadataFactory.get().noGravity());
        return data;
    }
}
