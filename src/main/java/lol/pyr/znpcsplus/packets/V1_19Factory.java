package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import lol.pyr.znpcsplus.entity.PacketEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.EnumSet;

public class V1_19Factory extends V1_14Factory {
    @Override
    public void addTabPlayer(Player player, PacketEntity entity) {
        if (entity.getType() != EntityTypes.PLAYER) return;
        WrapperPlayServerPlayerInfoUpdate.PlayerInfo info = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                skinned(entity, new UserProfile(entity.getUuid(), Integer.toString(entity.getEntityId()))), false,
                1, GameMode.CREATIVE, Component.empty(), null);
        sendPacket(player, new WrapperPlayServerPlayerInfoUpdate(EnumSet.of(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED), info, info));
    }

    @Override
    public void removeTabPlayer(Player player, PacketEntity entity) {
        if (entity.getType() != EntityTypes.PLAYER) return;
        sendPacket(player, new WrapperPlayServerPlayerInfoRemove(entity.getUuid()));
    }
}
