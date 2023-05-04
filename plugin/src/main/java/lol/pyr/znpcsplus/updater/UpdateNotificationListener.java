package lol.pyr.znpcsplus.updater;

import lol.pyr.znpcsplus.ZNpcsPlus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateNotificationListener implements Listener {
    private final ZNpcsPlus plugin;
    private final UpdateChecker updateChecker;

    public UpdateNotificationListener(ZNpcsPlus plugin, UpdateChecker updateChecker) {
        this.plugin = plugin;
        this.updateChecker = updateChecker;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("znpcsplus.updates")) return;
        if (updateChecker.getStatus() != UpdateChecker.Status.UPDATE_NEEDED) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!event.getPlayer().isOnline()) return;
            ZNpcsPlus.ADVENTURE.player(event.getPlayer())
                    .sendMessage(Component.text(plugin.getDescription().getName() + " v" + updateChecker.getLatestVersion() + " is available now!", NamedTextColor.GOLD).appendNewline()
                    .append(Component.text("Click this message to open the Spigot page (CLICK)", NamedTextColor.YELLOW)).clickEvent(ClickEvent.openUrl(UpdateChecker.DOWNLOAD_LINK)));
        }, 100L);
    }
}
