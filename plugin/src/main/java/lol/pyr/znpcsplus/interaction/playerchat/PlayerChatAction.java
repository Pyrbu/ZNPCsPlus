package lol.pyr.znpcsplus.interaction.playerchat;

import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.InteractionAction;
import org.bukkit.entity.Player;

public class PlayerChatAction extends InteractionAction {
    private final String message;

    public PlayerChatAction(String message, InteractionType interactionType, long delay) {
        super(delay, interactionType);
        this.message = message;
    }

    @Override
    public void run(Player player) {
        player.chat(message.replace("{player}", player.getName())
                .replace("{uuid}", player.getUniqueId().toString()));
    }

    public String getMessage() {
        return message;
    }
}
