package lol.pyr.znpcsplus.interaction.switchserver;

import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.util.BungeeConnector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class SwitchServerAction extends InteractionAction {
    private final BungeeConnector bungeeConnector;
    private final String server;

    public SwitchServerAction(BungeeConnector bungeeConnector, String server, InteractionType interactionType, long delay) {
        super(delay, interactionType);
        this.bungeeConnector = bungeeConnector;
        this.server = server;
    }

    @Override
    public void run(Player player) {
        bungeeConnector.sendPlayer(player, server);
    }

    @Override
    public Component getInfo(String id, int index, String label) {
        return Component.text(index + ") ", NamedTextColor.GOLD)
                .append(Component.text("[EDIT]", NamedTextColor.DARK_GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to edit this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + label + " action edit " + id + " " + index + " switcserver " + " " + getInteractionType().name() + " " + getCooldown()/1000 + " " + server))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("[DELETE]", NamedTextColor.RED)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to delete this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + label + " action delete " + id + " " + index)))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("Switch Server: ", NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click Type: " + getInteractionType().name() + " Cooldown: " + getCooldown()/1000, NamedTextColor.GREEN))))
                .append(Component.text(server, NamedTextColor.WHITE)));
    }

    public String getServer() {
        return server;
    }
}
