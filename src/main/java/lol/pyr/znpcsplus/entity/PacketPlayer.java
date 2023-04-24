package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import lol.pyr.znpcsplus.packets.PacketFactory;
import org.bukkit.entity.Player;

public class PacketPlayer extends PacketEntity {
    private final UserProfile gameProfile;

    public PacketPlayer(PacketLocation location) {
        super(EntityTypes.PLAYER, location);
        this.gameProfile = new UserProfile(getUuid(), Integer.toString(getEntityId()));
    }

    @Override
    public void spawn(Player player) {
        PacketFactory.get().spawnPlayer(player,  this);
    }

    public UserProfile getGameProfile() {
        return gameProfile;
    }
}
