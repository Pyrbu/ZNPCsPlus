package io.github.znetworkw.znpcservers.commands;

import io.github.znetworkw.znpcservers.exception.CommandException;
import io.github.znetworkw.znpcservers.exception.CommandExecuteException;
import io.github.znetworkw.znpcservers.exception.CommandPermissionException;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class CommandTabInvoker {
    private final Command command;
    private final Method commandTabMethod;
    private final String permission;

    public CommandTabInvoker(Command command, Method commandMethod, String permission) {
        this.command = command;
        this.commandTabMethod = commandMethod;
        this.permission = permission;
    }

    public List<String> tabComplete(CommandSender sender, Object command) throws CommandException {
        // Check if the sender is not a player
        if (!(sender.getCommandSender() instanceof Player))
            throw new CommandException("Only players may execute this command.");

        final Player player = (Player)sender.getCommandSender();

        // Check if the permission is not empty and the player does not have the permission
        if (this.permission.length() > 0 && !player.hasPermission(this.permission))
            throw new CommandPermissionException("You cannot execute this command.");

        try {
            // Command Tab execution
            Object invoke = this.commandTabMethod.invoke(this.command, sender, command);
            if (invoke instanceof List<?>)
                return ((List<String>) invoke);
        } catch (IllegalAccessException | ClassCastException | java.lang.reflect.InvocationTargetException e) {
            throw new CommandExecuteException(e.getMessage(), e.getCause());
        }

        return Collections.emptyList();
    }
}
