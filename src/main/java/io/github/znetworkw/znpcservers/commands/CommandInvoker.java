package io.github.znetworkw.znpcservers.commands;

import io.github.znetworkw.znpcservers.commands.exception.CommandException;
import io.github.znetworkw.znpcservers.commands.exception.CommandExecuteException;
import io.github.znetworkw.znpcservers.commands.exception.CommandPermissionException;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class CommandInvoker {
    private final Command command;
    private final Method commandMethod;
    private final String permission;

    public CommandInvoker(Command command, Method commandMethod, String permission) {
        this.command = command;
        this.commandMethod = commandMethod;
        this.permission = permission;
    }

    public void execute(CommandSender sender, Object command) throws CommandException {
        // Check if the sender is not a player
        if (!(sender instanceof Player player))
            throw new CommandException("Only players may execute this command.");

        // Check if the permission is not empty and the player does not have the permission
        if (this.permission.length() > 0 && !player.hasPermission(this.permission))
            throw new CommandPermissionException("You cannot execute this command.");

        try {
            // Command execution
            this.commandMethod.invoke(this.command, sender, command);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            throw new CommandExecuteException(e.getMessage(), e.getCause());
        }
    }
}
