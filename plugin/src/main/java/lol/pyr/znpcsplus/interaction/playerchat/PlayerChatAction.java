package lol.pyr.znpcsplus.interaction.playerchat;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.InteractionActionImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class PlayerChatAction extends InteractionActionImpl {
    private final String message;
    private final TaskScheduler scheduler;

    public PlayerChatAction(TaskScheduler scheduler, String message, InteractionType interactionType, long cooldown, long delay) {
        super(cooldown, delay, interactionType);
        this.message = message;
        this.scheduler = scheduler;
    }

    @Override
    public void run(Player player) {
        scheduler.schedulePlayerChat(player, message.replace("{player}", player.getName())
                .replace("{uuid}", player.getUniqueId().toString()));
    }

    @Override
    public Component getInfo(String id, int index, CommandContext context) {
        return Component.text(index + ") ", NamedTextColor.GOLD)
                .append(Component.text("[EDIT]", NamedTextColor.DARK_GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to edit this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + context.getLabel() + " action edit " + id + " " + index + " playerchat " + getInteractionType().name() + " " + getCooldown()/1000 + " " + getDelay() + " " + message))
                        .append(Component.text(" | ", NamedTextColor.GRAY))
                        .append(Component.text("[DELETE]", NamedTextColor.RED)
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.text("Click to delete this action", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                        "/" + context.getLabel() + " action delete " + id + " " + index)))
                        .append(Component.text(" | ", NamedTextColor.GRAY))
                        .append(Component.text("Player Chat: ", NamedTextColor.GREEN)
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.text("Click Type: " + getInteractionType().name() + " Cooldown: " + getCooldown()/1000 + " Delay: " + getDelay(), NamedTextColor.GRAY))))
                        .append(Component.text(message, NamedTextColor.WHITE)));
    }

    public String getMessage() {
        return message;
    }
}
