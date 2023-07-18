package lol.pyr.znpcsplus.util;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Viewable {
    private final Set<Player> viewers = ConcurrentHashMap.newKeySet();

    public void delete() {
        UNSAFE_hideAll();
        viewers.clear();
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
        UNSAFE_show(player);
        viewers.add(player);
    }

    public void hide(Player player) {
        if (!viewers.contains(player)) return;
        UNSAFE_hide(player);
        viewers.remove(player);
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
