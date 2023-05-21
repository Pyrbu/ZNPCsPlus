package lol.pyr.znpcsplus.util;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeConnector {
    private final Plugin plugin;

    public BungeeConnector(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendPlayer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(this.plugin, "BungeeCord", b.toByteArray());
    }
}
