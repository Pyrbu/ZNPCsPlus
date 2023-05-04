package lol.pyr.znpcsplus.interaction.types;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.interaction.NpcAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class MessageAction extends NpcAction {
    private final Component message;

    public MessageAction(long delay, String argument) {
        super(delay, argument);
        message = MiniMessage.miniMessage().deserialize(argument);
    }

    @Override
    public void run(Player player) {
        ZNpcsPlus.ADVENTURE.player(player).sendMessage(message);
    }
}
