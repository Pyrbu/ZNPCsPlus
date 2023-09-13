package lol.pyr.znpcsplus.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BungeeConnector {
    private final static String CHANNEL_NAME = "BungeeCord";
    private final Plugin plugin;

    public BungeeConnector(Plugin plugin) {
        this.plugin = plugin;
    }

    public void connectPlayer(Player player, String server) {
        player.sendPluginMessage(plugin, CHANNEL_NAME, createMessage("Connect", server));
    }

    @SuppressWarnings("UnstableApiUsage")
    private byte[] createMessage(String... parts) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String part : parts) out.writeUTF(part);
        return out.toByteArray();
    }

    public void registerChannel() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL_NAME);
    }

    public void unregisterChannel() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, CHANNEL_NAME);
    }
}
