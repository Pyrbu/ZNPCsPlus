package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.entity.PacketLocation;
import org.bukkit.entity.Player;

import java.util.Optional;

public class V1_14Factory extends V1_8Factory {
    @Override
    public void spawnEntity(Player player, PacketEntity entity) {
        PacketLocation location = entity.getLocation();
        sendPacket(player, new WrapperPlayServerSpawnEntity(entity.getEntityId(), Optional.of(entity.getUuid()), entity.getType(),
                location.toVector3d(), location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.empty()));
    }
}
