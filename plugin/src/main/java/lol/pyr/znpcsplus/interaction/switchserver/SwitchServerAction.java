package lol.pyr.znpcsplus.interaction.switchserver;

import lol.pyr.znpcsplus.interaction.InteractionAction;
import lol.pyr.znpcsplus.util.BungeeConnector;
import org.bukkit.entity.Player;

public class SwitchServerAction extends InteractionAction {
    private final BungeeConnector bungeeConnector;
    private final String server;

    public SwitchServerAction(BungeeConnector bungeeConnector, String server, long delay) {
        super(delay);
        this.bungeeConnector = bungeeConnector;
        this.server = server;
    }

    @Override
    public void run(Player player) {
        bungeeConnector.sendPlayer(player, server);
    }

    public String getServer() {
        return server;
    }
}
