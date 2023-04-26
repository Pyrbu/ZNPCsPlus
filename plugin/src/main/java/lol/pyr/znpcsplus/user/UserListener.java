package lol.pyr.znpcsplus.user;

import io.github.znetworkw.znpcservers.user.EventService;
import lol.pyr.znpcsplus.ZNPCsPlus;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {
    public UserListener(ZNPCsPlus plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        User.get(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        User.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTalk(AsyncPlayerChatEvent event) {
        User zUser = User.get(event.getPlayer());
        if (EventService.hasService(zUser, AsyncPlayerChatEvent.class)) {
            event.setCancelled(true);
            EventService<AsyncPlayerChatEvent> eventService = EventService.findService(zUser, AsyncPlayerChatEvent.class);
            eventService.runAll(event);
            zUser.getEventServices().remove(eventService);
        }
    }
}
