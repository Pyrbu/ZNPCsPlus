package lol.pyr.znpcsplus.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

public abstract class Viewable {
  private static final List<WeakReference<Viewable>> all =
      Collections.synchronizedList(new ArrayList<>());

  public static List<Viewable> all() {
    synchronized (all) {
      all.removeIf(reference -> reference.get() == null);
      return all.stream().map(Reference::get).collect(Collectors.toList());
    }
  }

  public static void shutdownExecutor() {
    visibilityExecutor.shutdown();
  }

  private static final ExecutorService visibilityExecutor = Executors.newSingleThreadExecutor();
  private final Set<Player> viewers = ConcurrentHashMap.newKeySet();

  public Viewable() {
    all.add(new WeakReference<>(this));
  }

  public void delete() {
    visibilityExecutor.submit(
        () -> {
          UNSAFE_hideAll();
          viewers.clear();
          synchronized (all) {
            all.removeIf(reference -> reference.get() == null || reference.get() == this);
          }
        });
  }

  public CompletableFuture<Void> respawn() {
    CompletableFuture<Void> future = new CompletableFuture<>();
    visibilityExecutor.submit(
        () -> {
          UNSAFE_hideAll();
          UNSAFE_showAll().thenRun(() -> future.complete(null));
        });
    return future;
  }

  public CompletableFuture<Void> respawn(Player player) {
    hide(player);
    return show(player);
  }

  public CompletableFuture<Void> show(Player player) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    visibilityExecutor.submit(
        () -> {
          if (viewers.contains(player)) {
            future.complete(null);
            return;
          }
          viewers.add(player);
          UNSAFE_show(player).thenRun(() -> future.complete(null));
        });
    return future;
  }

  public void hide(Player player) {
    visibilityExecutor.submit(
        () -> {
          if (!viewers.contains(player)) return;
          viewers.remove(player);
          UNSAFE_hide(player);
        });
  }

  public void UNSAFE_removeViewer(Player player) {
    viewers.remove(player);
  }

  protected void UNSAFE_hideAll() {
    for (Player viewer : viewers) UNSAFE_hide(viewer);
  }

  protected CompletableFuture<Void> UNSAFE_showAll() {
    return FutureUtil.allOf(viewers.stream().map(this::UNSAFE_show).collect(Collectors.toList()));
  }

  public Set<Player> getViewers() {
    return Collections.unmodifiableSet(viewers);
  }

  public boolean isVisibleTo(Player player) {
    return viewers.contains(player);
  }

  protected abstract CompletableFuture<Void> UNSAFE_show(Player player);

  protected abstract void UNSAFE_hide(Player player);
}
