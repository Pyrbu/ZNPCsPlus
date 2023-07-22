package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class SkinProperty extends EntityPropertyImpl<SkinDescriptor> {
    public SkinProperty() {
        super("skin", null, SkinDescriptor.class);
    }

    @Override
    public void apply(SkinDescriptor value, Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {

    }
}
