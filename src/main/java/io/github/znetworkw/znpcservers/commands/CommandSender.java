package io.github.znetworkw.znpcservers.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.utility.Utils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class CommandSender {
    static final Joiner LINE_SEPARATOR_JOINER = Joiner.on("\n");

    private static final ImmutableList<String> HELP_PREFIX = ImmutableList.of("&6&lEXAMPLES&r:");

    private final org.bukkit.command.CommandSender commandSender;

    private final SenderType type;

    public CommandSender(org.bukkit.command.CommandSender commandSender) {
        this.commandSender = commandSender;
        this.type = (commandSender instanceof Player) ? SenderType.PLAYER : SenderType.CONSOLE;
    }

    public void sendMessage(String message) {
        sendMessage(message, null);
    }

    public void sendMessage(CommandInformation subCommand) {
        sendMessage(" &7\u00BB &6/&eznpcs " + subCommand.name() + " " +
                        Arrays.stream(subCommand.arguments())
                                .map(s -> "<" + s + ">")
                                .collect(Collectors.joining(" ")),
                Arrays.asList(subCommand.help()));
    }

    public void sendMessage(String message, Iterable<String> hover) {
        TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(Utils.toColor(message)));
        if (hover != null)
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(
                    Utils.toColor(LINE_SEPARATOR_JOINER
                            .join(Iterables.concat(HELP_PREFIX, hover)))))
                    .create()));
        getPlayer().spigot().sendMessage(textComponent);
    }

    public Player getPlayer() {
        if (this.type != SenderType.PLAYER)
            throw new IllegalStateException("The following sender is not a player.");
        return (Player) getCommandSender();
    }

    public org.bukkit.command.CommandSender getCommandSender() {
        return this.commandSender;
    }

    enum SenderType {
        PLAYER, CONSOLE
    }
}
