package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.util.ZLocation;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import org.bukkit.entity.Player;

import java.util.Optional;

public class V1_14Factory extends V1_10Factory {
    @Override
    public void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        ZLocation location = entity.getLocation();
        sendPacket(player, new WrapperPlayServerSpawnEntity(entity.getEntityId(), Optional.of(entity.getUuid()), entity.getType(),
                location.toVector3d(), location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.of(new Vector3d())));
        if (properties.hasProperty(EntityPropertyImpl.GLOW)) createTeam(player, entity, properties);
        sendAllMetadata(player, entity, properties);
        createTeam(player, entity, properties);
    }
}
