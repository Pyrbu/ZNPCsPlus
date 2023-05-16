package lol.pyr.znpcsplus.interaction.types;

import lol.pyr.znpcsplus.interaction.InteractionAction;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.PapiUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleCommandAction extends InteractionAction {
    private final TaskScheduler scheduler;
    private final String command;

    public ConsoleCommandAction(TaskScheduler scheduler, String command, long delay) {
        super(delay);
        this.scheduler = scheduler;
        this.command = command;
    }

    @Override
    public void run(Player player) {
        String cmd = command.replace("{player}", player.getName()).replace("{uuid}", player.getUniqueId().toString());
        scheduler.runSync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PapiUtil.set(player, cmd)));
    }

    public String getCommand() {
        return command;
    }
}
