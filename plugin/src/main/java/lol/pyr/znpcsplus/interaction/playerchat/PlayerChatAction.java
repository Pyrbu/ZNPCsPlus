package lol.pyr.znpcsplus.interaction.playerchat;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.InteractionAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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

    @Override
    public Component getInfo(String id, int index, CommandContext context) {
        return Component.text(index + ") ", NamedTextColor.GOLD)
                .append(Component.text("[EDIT]", NamedTextColor.DARK_GREEN, TextDecoration.BOLD)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to edit this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + context.getLabel() + " action edit " + id + " " + index + " playerchat " + " " + getInteractionType().name() + " " + getCooldown()/1000 + " " + message))
                        .append(Component.text(" | ", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
                        .append(Component.text("[DELETE]", NamedTextColor.RED, TextDecoration.BOLD)
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.text("Click to delete this action", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                        "/" + context.getLabel() + " action delete " + id + " " + index)))
                        .append(Component.text(" | ", NamedTextColor.GRAY).style(style -> style.decoration(TextDecoration.BOLD, false)))
                        .append(Component.text("Player Chat: ", NamedTextColor.GREEN)
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.text("Click Type: " + getInteractionType().name() + " Cooldown: " + getCooldown()/1000, NamedTextColor.GREEN))))
                        .append(Component.text(message, NamedTextColor.WHITE)));
    }

    public String getMessage() {
        return message;
    }
}
