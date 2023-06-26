package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ReloadConfigCommand implements CommandHandler {
    private final ConfigManager configManager;

    public ReloadConfigCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        configManager.reload();
        context.send(Component.text("Plugin configuration reloaded successfully", NamedTextColor.GREEN));
    }
}
