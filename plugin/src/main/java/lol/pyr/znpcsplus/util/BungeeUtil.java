package lol.pyr.znpcsplus.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

public class BungeeUtil {
    public static void connectPlayer(Player player, String server) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerPluginMessage("BungeeCord", createMessage("Connect", server)));
    }

    @SuppressWarnings("UnstableApiUsage")
    private static byte[] createMessage(String... parts) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String part : parts) out.writeUTF(part);
        return out.toByteArray();
    }
}
