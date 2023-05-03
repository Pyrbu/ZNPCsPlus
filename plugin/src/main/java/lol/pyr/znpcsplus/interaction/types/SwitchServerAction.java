package lol.pyr.znpcsplus.interaction.types;

import lol.pyr.znpcsplus.ZNPCsPlus;
import lol.pyr.znpcsplus.interaction.NPCAction;
import org.bukkit.entity.Player;

public class SwitchServerAction extends NPCAction {
    public SwitchServerAction(long delay, String argument) {
        super(delay, argument);
    }

    @Override
    public void run(Player player) {
        ZNPCsPlus.BUNGEE_UTILS.sendPlayerToServer(player, argument);
    }
}
