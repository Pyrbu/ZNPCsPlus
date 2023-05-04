package lol.pyr.znpcsplus.interaction.types;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.interaction.NpcAction;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerCommandAction extends NpcAction {
    public PlayerCommandAction(long delay, String argument) {
        super(delay, argument);
    }

    @Override
    public void run(Player player) {
        String cmd = argument.replace("{player}", player.getName()).replace("{uuid}", player.getUniqueId().toString());
        Bukkit.dispatchCommand(player, ZNpcsPlus.PLACEHOLDERS_SUPPORTED ? PlaceholderAPI.setPlaceholders(player, cmd) : cmd);
    }
}
