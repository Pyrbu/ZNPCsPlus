package lol.pyr.znpcsplus.interaction.message;

import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.InteractionAction;
import lol.pyr.znpcsplus.util.PapiUtil;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class MessageAction extends InteractionAction {
    private final BukkitAudiences adventure;
    private final String message;
    private final LegacyComponentSerializer textSerializer;

    public MessageAction(BukkitAudiences adventure, String message, InteractionType interactionType, LegacyComponentSerializer textSerializer, long delay) {
        super(delay, interactionType);
        this.adventure = adventure;
        this.message = message;
        this.textSerializer = textSerializer;
    }

    @Override
    public void run(Player player) {
        String msg = message.replace("{player}", player.getName())
                .replace("{uuid}", player.getUniqueId().toString());
        adventure.player(player).sendMessage(textSerializer.deserialize(PapiUtil.set(player, msg)));
    }

    public String getMessage() {
        return message;
    }
}
