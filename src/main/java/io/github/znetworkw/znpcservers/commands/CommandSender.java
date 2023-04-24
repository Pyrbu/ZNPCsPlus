package io.github.znetworkw.znpcservers.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import lol.pyr.znpcsplus.ZNPCsPlus;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

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
        message = message.replace(LegacyComponentSerializer.SECTION_CHAR, LegacyComponentSerializer.AMPERSAND_CHAR);
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        TextComponent textComponent = serializer.deserialize(message);
        if (hover != null) textComponent = textComponent.hoverEvent(serializer.deserialize(LINE_SEPARATOR_JOINER.join(Iterables.concat(HELP_PREFIX, hover))));
        ZNPCsPlus.ADVENTURE.player(getPlayer()).sendMessage(textComponent);
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
