package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class V1_9Factory extends V1_8Factory {
    @Override
    public void sendAllMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        ArrayList<EntityData> data = new ArrayList<>();
        if (entity.getType() == EntityTypes.PLAYER) data.add(MetadataFactory.get().skinLayers(properties.getProperty(EntityPropertyImpl.SKIN_LAYERS)));
        data.add(MetadataFactory.get().effects(properties.getProperty(EntityPropertyImpl.FIRE), properties.hasProperty(EntityPropertyImpl.GLOW), properties.getProperty(EntityPropertyImpl.INVISIBLE)));
        data.add(MetadataFactory.get().silent(properties.getProperty(EntityPropertyImpl.SILENT)));
        if (properties.hasProperty(EntityPropertyImpl.NAME)) data.addAll(MetadataFactory.get().name(properties.getProperty(EntityPropertyImpl.NAME)));
        sendMetadata(player, entity, data);
    }
}
