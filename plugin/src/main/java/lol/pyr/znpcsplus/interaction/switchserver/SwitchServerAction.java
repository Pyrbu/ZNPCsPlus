package lol.pyr.znpcsplus.interaction.switchserver;

import lol.pyr.znpcsplus.interaction.InteractionAction;
import lol.pyr.znpcsplus.util.BungeeUtil;
import org.bukkit.entity.Player;

public class SwitchServerAction extends InteractionAction {
    private final BungeeUtil bungeeUtil;
    private final String server;

    public SwitchServerAction(BungeeUtil bungeeUtil, String server, long delay) {
        super(delay);
        this.bungeeUtil = bungeeUtil;
        this.server = server;
    }

    @Override
    public void run(Player player) {
        bungeeUtil.sendPlayerToServer(player, server);
    }

    public String getServer() {
        return server;
    }
}
