package io.github.znetworkw.znpcservers.commands;

import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Method;
import java.util.*;

public class Command extends BukkitCommand {
    private static final String WHITESPACE = " ";

    private static final CommandMap COMMAND_MAP;

    static {
        try {
            COMMAND_MAP = (CommandMap) CacheRegistry.BUKKIT_COMMAND_MAP.load().get(Bukkit.getServer());
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("can't access bukkit command map.");
        }
    }

    private final Map<CommandInformation, CommandInvoker> subCommands;

    public Command(String name) {
        super(name);
        this.subCommands = new HashMap<>();
        load();
    }

    private void load() {
        COMMAND_MAP.register(getName(), this);
        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(CommandInformation.class)) {
                CommandInformation cmdInfo = method.getAnnotation(CommandInformation.class);
                this.subCommands.put(cmdInfo, new CommandInvoker(this, method, cmdInfo.permission()));
            }
        }
    }

    private Map<String, String> loadArgs(CommandInformation subCommand, Iterable<String> args) {
        int size = Iterables.size(args);
        int subCommandsSize = (subCommand.arguments()).length;
        Map<String, String> argsMap = new HashMap<>();
        if (size > 1)
            if (subCommand.isMultiple()) {
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

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Optional<Map.Entry<CommandInformation, CommandInvoker>> subCommandOptional = this.subCommands.entrySet().stream().filter(command -> command.getKey().name().contentEquals((args.length > 0) ? args[0] : "")).findFirst();
        if (!subCommandOptional.isPresent()) {
            sender.sendMessage(ChatColor.RED + "can't find command: " + commandLabel + ".");
            return false;
        }
        try {
            Map.Entry<CommandInformation, CommandInvoker> subCommand = subCommandOptional.get();
            ((CommandInvoker) subCommand.getValue()).execute(new CommandSender(sender),

                    loadArgs(subCommand.getKey(), Arrays.asList(args)));
        } catch (CommandExecuteException e) {
            sender.sendMessage(ChatColor.RED + "can't execute command.");
            e.printStackTrace();
        } catch (CommandPermissionException e) {
            sender.sendMessage(ChatColor.RED + "no permission for run this command.");
        }
        return true;
    }
}
