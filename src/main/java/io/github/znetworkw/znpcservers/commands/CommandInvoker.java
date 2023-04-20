package io.github.znetworkw.znpcservers.commands;

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

    public void execute(CommandSender sender, Object command) throws CommandPermissionException, CommandExecuteException {
        if (!(sender instanceof Player) && this.permission.length() == 0) {
            throw new CommandPermissionException("Only players may execute this command.");
        }
        if (this.permission.length() > 0 && !sender.getCommandSender().hasPermission(this.permission)) {
            throw new CommandPermissionException("You cannot execute this command.");
        }
        try {
            this.commandMethod.invoke(this.command, sender, command);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            throw new CommandExecuteException(e.getMessage(), e.getCause());
        }
    }
}
