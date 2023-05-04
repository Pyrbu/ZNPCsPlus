package lol.pyr.znpcsplus.interaction.types;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.interaction.NpcAction;
import org.bukkit.entity.Player;

public class SwitchServerAction extends NpcAction {
    public SwitchServerAction(long delay, String argument) {
        super(delay, argument);
    }

    @Override
    public void run(Player player) {
        ZNpcsPlus.BUNGEE_UTILS.sendPlayerToServer(player, argument);
    }
}
