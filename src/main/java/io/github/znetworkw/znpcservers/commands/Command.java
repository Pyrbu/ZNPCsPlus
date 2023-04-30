package io.github.znetworkw.znpcservers.commands;

import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.exception.CommandException;
import io.github.znetworkw.znpcservers.exception.CommandExecuteException;
import io.github.znetworkw.znpcservers.exception.CommandPermissionException;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Command extends BukkitCommand {
    private final Map<CommandInformation, CommandInvoker> subCommands;
    private final Map<CommandTabInformation, CommandTabInvoker> subCommandsTab;

    public Command(String name) {
        super(name);
        this.subCommands = new HashMap<>();
        this.subCommandsTab = new HashMap<>();
        load();
    }

    private void load() {
        Reflections.COMMAND_MAP_FIELD.get().register(getName(), this);
        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(CommandInformation.class)) {
                CommandInformation cmdInfo = method.getAnnotation(CommandInformation.class);
                this.subCommands.put(cmdInfo, new CommandInvoker(this, method, cmdInfo.permission()));
            }
            if (method.isAnnotationPresent(CommandTabInformation.class)) {
                CommandTabInformation cmdTabInfo = method.getAnnotation(CommandTabInformation.class);
                this.subCommandsTab.put(cmdTabInfo, new CommandTabInvoker(this, method, cmdTabInfo.permission()));
            }
        }
    }

    private Map<String, String> loadArgs(CommandInformation subCommand, Iterable<String> args) {
        int size = Iterables.size(args);
        int subCommandsSize = (subCommand.arguments()).length;
        Map<String, String> argsMap = new HashMap<>();
        if (size > 1) if (subCommand.isMultiple()) {
            argsMap.put(Iterables.get(args, 1), String.join(" ", Iterables.skip(args, 2)));
        } else {
            for (int i = 0; i < Math.min(subCommandsSize, size); i++) {
                int fixedLength = i + 1;
                if (size > fixedLength) {
                    String input = Iterables.get(args, fixedLength);
                    if (fixedLength == subCommandsSize)
                        input = String.join(" ", Iterables.skip(args, subCommandsSize));
                    argsMap.put(subCommand.arguments()[i], input);
                }
            }
        }
        return argsMap;
    }

    public Set<CommandInformation> getCommands() {
        return this.subCommands.keySet();
    }

    public Set<CommandTabInformation> getCommandsTab() {
        return this.subCommandsTab.keySet();
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Optional<Map.Entry<CommandInformation, CommandInvoker>> subCommandOptional = this.subCommands.entrySet().stream().filter(command -> command.getKey().name().contentEquals((args.length > 0) ? args[0] : "")).findFirst();
        if (subCommandOptional.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Unable to locate the following command: " + commandLabel + ".");
            return false;
        }
        try {
            Map.Entry<CommandInformation, CommandInvoker> subCommand = subCommandOptional.get();
            subCommand.getValue().execute(new io.github.znetworkw.znpcservers.commands.CommandSender(sender), loadArgs(subCommand.getKey(), Arrays.asList(args)));
        } catch (CommandExecuteException e) {
            sender.sendMessage(ChatColor.RED + "Cannot execute this command or this command execution has failed.");
            e.printStackTrace();
        } catch (CommandPermissionException e) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    public @NotNull List<String> tabComplete(CommandSender sender, String commandLabel, String[] args) {
        Optional<Map.Entry<CommandTabInformation, CommandTabInvoker>> subCommandOptional = this.subCommandsTab.entrySet().stream().filter(command -> command.getKey().name().contentEquals((args.length > 0) ? args[0] : "")).findFirst();
        if (subCommandOptional.isEmpty()) {
            List<String> collect = getCommands().stream().map(CommandInformation::name).collect(Collectors.toList());
            if (args.length == 0) {
                return collect;
            }
            return StringUtil.copyPartialMatches(args[0], collect, new ArrayList<>());
        }
        try {
            Map.Entry<CommandTabInformation, CommandTabInvoker> subCommand = subCommandOptional.get();
            if (args.length == 0) {
                return subCommand.getValue().tabComplete(new io.github.znetworkw.znpcservers.commands.CommandSender(sender), args);
            }
            return subCommand.getValue().tabComplete(new io.github.znetworkw.znpcservers.commands.CommandSender(sender), Arrays.copyOfRange(args, 1, args.length));
        } catch (CommandException e) {
            return List.of();
        }
    }
}
