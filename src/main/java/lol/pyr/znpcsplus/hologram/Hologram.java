package lol.pyr.znpcsplus.hologram;

import lol.pyr.znpcsplus.config.Configs;
import lol.pyr.znpcsplus.entity.PacketLocation;
import lol.pyr.znpcsplus.util.Viewable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Hologram extends Viewable {
    private PacketLocation location;
    private final List<HologramLine> lines = new ArrayList<>();

    public Hologram(PacketLocation location) {
        this.location = location;
    }

    public void addLine(Component line) {
        HologramLine newLine = new HologramLine(null, line);
        lines.add(newLine);
        relocateLines();
        for (Player viewer : getViewers()) newLine.show(viewer.getPlayer());
    }

    public HologramLine getLine(int index) {
        return lines.get(index);
    }

    public void removeLine(int index) {
        HologramLine line = lines.remove(index);
        for (Player viewer : getViewers()) line.hide(viewer);
        relocateLines();
    }

    public void clearLines() {
        UNSAFE_hideAll();
        lines.clear();
    }

    public void insertLine(int index, Component line) {
        HologramLine newLine = new HologramLine(null, line);
        lines.add(index, newLine);
        relocateLines();
        for (Player viewer : getViewers()) newLine.show(viewer.getPlayer());
    }

    @Override
    protected void _show(Player player) {
        for (HologramLine line : lines) line.show(player);
    }

    @Override
    protected void _hide(Player player) {
        for (HologramLine line : lines) line.hide(player);
    }

    public void setLocation(PacketLocation location) {
        this.location = location;
        relocateLines();
    }

    private void relocateLines() {
        relocateLines(null);
    }

    private void relocateLines(HologramLine newLine) {
        final double lineSpacing = Configs.config().lineSpacing();
        double height = location.getY() + lines.size() * lineSpacing;
        for (HologramLine line : lines) {
            line.setLocation(location.withY(height), line == newLine ? Set.of() : getViewers());
            height -= lineSpacing;
        }
    }
}
