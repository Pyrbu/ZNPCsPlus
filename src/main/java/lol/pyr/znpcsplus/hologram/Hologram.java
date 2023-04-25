package lol.pyr.znpcsplus.hologram;

import lol.pyr.znpcsplus.util.Viewable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Hologram extends Viewable {
    private final List<HologramLine> lines = new ArrayList<>();

    public Hologram() {

    }

    public void addLine(Component line) {
        lines.add(new HologramLine(null, line)); // TODO: Location
    }

    public HologramLine getLine(int index) {
        return lines.get(index);
    }

    public void insertLine(int index, Component line) {
        lines.add(index, new HologramLine(null, line)); // TODO: Location
    }

    @Override
    protected void _show(Player player) {
        for (HologramLine line : lines) line.show(player);
    }

    @Override
    protected void _hide(Player player) {
        for (HologramLine line : lines) line.hide(player);
    }
}
