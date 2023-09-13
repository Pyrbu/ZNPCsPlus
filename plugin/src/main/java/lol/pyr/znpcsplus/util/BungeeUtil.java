package lol.pyr.znpcsplus.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BungeeUtil {
    private final static String CHANNEL_NAME = "BungeeCord";

    public static void connectPlayer(Player player, String server) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerPluginMessage(CHANNEL_NAME, createMessage("Connect", server)));
    }

    @SuppressWarnings("UnstableApiUsage")
    private static byte[] createMessage(String... parts) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String part : parts) out.writeUTF(part);
        return out.toByteArray();
    }

    public static void registerChannel(Plugin plugin) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL_NAME);
    }

    public static void unregisterChannel(Plugin plugin) {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, CHANNEL_NAME);
    }
}
