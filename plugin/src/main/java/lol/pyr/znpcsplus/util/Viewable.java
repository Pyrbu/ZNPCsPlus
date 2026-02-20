package lol.pyr.znpcsplus.util;

import org.bukkit.entity.Player;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public abstract class Viewable {
    private static final List<WeakReference<Viewable>> all = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService visibilityExecutor = Executors.newSingleThreadExecutor();

    private final Set<Player> viewers = ConcurrentHashMap.newKeySet();

    public Viewable() {
        all.add(new WeakReference<>(this));
    }

    public static List<Viewable> all() {
        synchronized (all) {
            all.removeIf(ref -> ref.get() == null);
            return all.stream()
                    .map(Reference::get)
                    .collect(Collectors.toList());
        }
    }

    public static void shutdownExecutor() {
        visibilityExecutor.shutdown();
    }

    public void delete() {
        visibilityExecutor.submit(() -> {
            UNSAFE_hideAll();
            viewers.clear();
            synchronized (all) {
                all.removeIf(ref -> ref.get() == null || ref.get() == this);
            }
        });
    }

    public CompletableFuture<Void> respawn() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        visibilityExecutor.submit(() -> {
            UNSAFE_hideAll();
            UNSAFE_showAll()
                    .whenComplete((v, ex) -> {
                        if (ex != null) future.completeExceptionally(ex);
                        else future.complete(null);
                    });
        });
        return future;
    }

    public CompletableFuture<Void> respawn(Player player) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        visibilityExecutor.submit(() -> {
            if (!viewers.contains(player)) {
                // Not visible — just show
                viewers.add(player);
                UNSAFE_show(player)
                        .whenComplete((v, ex) -> {
                            if (ex != null) future.completeExceptionally(ex);
                            else future.complete(null);
                        });
                return;
            }
            viewers.remove(player);
            // Wait for hide to fully complete, THEN show
            UNSAFE_hideAndShow(player)
                    .whenComplete((v, ex) -> {
                        if (ex != null) future.completeExceptionally(ex);
                        else future.complete(null);
                    });
        });
        return future;
    }

    public CompletableFuture<Void> show(Player player) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        visibilityExecutor.submit(() -> {
            if (viewers.contains(player)) {
                future.complete(null);
                return;
            }
            viewers.add(player);
            UNSAFE_show(player)
                    .whenComplete((v, ex) -> {
                        if (ex != null) future.completeExceptionally(ex);
                        else future.complete(null);
                    });
        });
        return future;
    }

    public void hide(Player player) {
        visibilityExecutor.submit(() -> {
            if (!viewers.remove(player)) return;
            UNSAFE_hide(player);
        });
    }

    public void UNSAFE_removeViewer(Player player) {
        viewers.remove(player);
    }

    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(viewers);
    }

    public boolean isVisibleTo(Player player) {
        return viewers.contains(player);
    }

    protected void UNSAFE_hideAll() {
        viewers.forEach(this::UNSAFE_hide);
    }

    protected CompletableFuture<Void> UNSAFE_showAll() {
        List<CompletableFuture<?>> futures = viewers.stream()
                .map(this::UNSAFE_show)
                .collect(Collectors.toList());
        return FutureUtil.allOf(futures);
    }

    protected CompletableFuture<Void> UNSAFE_hideAndShow(Player player) {
        UNSAFE_hide(player);
        viewers.add(player);
        return UNSAFE_show(player);
    }

    protected abstract CompletableFuture<Void> UNSAFE_show(Player player);

    protected abstract void UNSAFE_hide(Player player);
}
