package lol.pyr.znpcsplus.interaction.switchserver;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.InteractionActionImpl;
import lol.pyr.znpcsplus.util.BungeeConnector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class SwitchServerAction extends InteractionActionImpl {
    private final String server;
    private final BungeeConnector bungeeConnector;

    public SwitchServerAction(String server, InteractionType interactionType, long cooldown, long delay, BungeeConnector bungeeConnector) {
        super(cooldown, delay, interactionType);
        this.server = server;
        this.bungeeConnector = bungeeConnector;
    }

    @Override
    public void run(Player player) {
        bungeeConnector.connectPlayer(player, server);
    }

    @Override
    public Component getInfo(String id, int index, CommandContext context) {
        return Component.text(index + ") ", NamedTextColor.GOLD)
                .append(Component.text("[EDIT]", NamedTextColor.DARK_GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to edit this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + context.getLabel() + " action edit " + id + " " + index + " switcserver " + getInteractionType().name() + " " + getCooldown()/1000 + " " + getDelay() + " " + server))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("[DELETE]", NamedTextColor.RED)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to delete this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + context.getLabel() + " action delete " + id + " " + index)))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("Switch Server: ", NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click Type: " + getInteractionType().name() + " Cooldown: " + getCooldown()/1000 + " Delay: " + getDelay(), NamedTextColor.GRAY))))
                .append(Component.text(server, NamedTextColor.WHITE)));
    }

    public String getServer() {
        return server;
    }
}
