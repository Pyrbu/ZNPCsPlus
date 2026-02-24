package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.util.Viewable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ViewableCleanupListener implements Listener {
  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Viewable.all()
        .forEach(
            viewable -> {
              if (viewable.isVisibleTo(event.getPlayer()))
                viewable.UNSAFE_removeViewer(event.getPlayer());
            });
  }

  @EventHandler
  public void onRespawn(PlayerRespawnEvent event) {
    Viewable.all()
        .forEach(
            viewable -> {
              if (viewable.isVisibleTo(event.getPlayer()))
                viewable.UNSAFE_removeViewer(event.getPlayer());
            });
  }
}
