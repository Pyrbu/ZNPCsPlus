package lol.pyr.znpcsplus.util;

import org.bukkit.entity.Player;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class Viewable {
    private final static List<WeakReference<Viewable>> all = new ArrayList<>();

    public static List<Viewable> all() {
        all.removeIf(reference -> reference.get() == null);
        return all.stream()
                .map(Reference::get)
                .collect(Collectors.toList());
    }

    private final Set<Player> viewers = ConcurrentHashMap.newKeySet();
    private final Set<Player> blacklisted = ConcurrentHashMap.newKeySet();

    public Viewable() {
        all.add(new WeakReference<>(this));
    }

    public void delete() {
        UNSAFE_hideAll();
        viewers.clear();
        blacklisted.clear();
    }

    public void respawn() {
        UNSAFE_hideAll();
        UNSAFE_showAll();
    }

    public void respawn(Player player) {
        if (!viewers.contains(player)) return;
        UNSAFE_hide(player);
        UNSAFE_show(player);
    }

    public void show(Player player) {
        if (viewers.contains(player)) return;
        viewers.add(player);
        UNSAFE_show(player);
    }

    public void hide(Player player) {
        if (!viewers.contains(player)) return;
        viewers.remove(player);
        UNSAFE_hide(player);
    }

    public void blacklist(Player player) {
        blacklisted.add(player);
        hide(player);
    }

    public void unblacklist(Player player) {
        blacklisted.remove(player);
    }

    public boolean isBlacklisted(Player player) {
        return blacklisted.contains(player);
    }

    public void UNSAFE_removeViewer(Player player) {
        viewers.remove(player);
        blacklisted.remove(player);
    }

    protected void UNSAFE_hideAll() {
        for (Player viewer : viewers) UNSAFE_hide(viewer);
    }

    protected void UNSAFE_showAll() {
        for (Player viewer : viewers) UNSAFE_show(viewer);
    }

    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(viewers);
    }

    public boolean isVisibleTo(Player player) {
        return viewers.contains(player);
    }

    protected abstract void UNSAFE_show(Player player);

    protected abstract void UNSAFE_hide(Player player);
}
