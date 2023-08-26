package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.util.Viewable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ViewableHideOnLeaveListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Viewable.all().forEach(viewable -> {
            if (viewable.isVisibleTo(event.getPlayer())) viewable.UNSAFE_removeViewer(event.getPlayer());
        });
    }
}
