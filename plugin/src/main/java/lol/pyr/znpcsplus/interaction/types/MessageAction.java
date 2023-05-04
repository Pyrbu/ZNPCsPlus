package lol.pyr.znpcsplus.interaction.types;

import lol.pyr.znpcsplus.ZNPCsPlus;
import lol.pyr.znpcsplus.interaction.NPCAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class MessageAction extends NPCAction {
    private final Component message;

    public MessageAction(long delay, String argument) {
        super(delay, argument);
        message = MiniMessage.miniMessage().deserialize(argument);
    }

    @Override
    public void run(Player player) {
        ZNPCsPlus.ADVENTURE.player(player).sendMessage(message);
    }
}
