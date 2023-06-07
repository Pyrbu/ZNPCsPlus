package lol.pyr.znpcsplus.interaction.playercommand;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.InteractionAction;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.PapiUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public class PlayerCommandAction extends InteractionAction {
    private final TaskScheduler scheduler;
    private final String command;

    public PlayerCommandAction(TaskScheduler scheduler, String command, InteractionType interactionType, long delay) {
        super(delay, interactionType);
        this.scheduler = scheduler;
        this.command = command;
    }

    @Override
    public void run(Player player) {
        String cmd = command.replace("{player}", player.getName()).replace("{uuid}", player.getUniqueId().toString());
        scheduler.schedulePlayerCommand(player, PapiUtil.set(player, cmd));
    }

    @Override
    public Component getInfo(String id, int index, CommandContext context) {
        return Component.text(index + ") ", NamedTextColor.GOLD)
                .append(Component.text("[EDIT]", NamedTextColor.DARK_GREEN, TextDecoration.BOLD)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to edit this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + context.getLabel() + " action edit " + id + " " + index + " playercommand " + " " + getInteractionType().name() + " " + getCooldown()/1000 + " " + command))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("[DELETE]", NamedTextColor.RED, TextDecoration.BOLD)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to delete this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + context.getLabel() + " action delete " + id + " " + index)))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("Player Command: ", NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click Type: " + getInteractionType().name() + " Cooldown: " + getCooldown()/1000, NamedTextColor.GREEN))))
                .append(Component.text(command, NamedTextColor.WHITE)));
    }

    public String getCommand() {
        return command;
    }
}
