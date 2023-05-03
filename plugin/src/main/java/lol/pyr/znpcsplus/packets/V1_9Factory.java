package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class V1_9Factory extends V1_8Factory {
    @Override
    public void sendAllMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        ArrayList<EntityData> data = new ArrayList<>();
        if (entity.getType() == EntityTypes.PLAYER && properties.getProperty(EntityProperty.SKIN_LAYERS)) data.add(MetadataFactory.get().skinLayers());
        boolean glow = properties.hasProperty(EntityProperty.GLOW);
        boolean fire = properties.getProperty(EntityProperty.FIRE);
        boolean invisible = properties.getProperty(EntityProperty.INVISIBLE);
        if (glow || fire || invisible) data.add(MetadataFactory.get().effects(fire, glow, invisible));
        if (properties.getProperty(EntityProperty.SILENT)) data.add(MetadataFactory.get().silent());
        if (properties.hasProperty(EntityProperty.NAME)) data.addAll(MetadataFactory.get().name(properties.getProperty(EntityProperty.NAME)));
        sendMetadata(player, entity, data);
    }
}
