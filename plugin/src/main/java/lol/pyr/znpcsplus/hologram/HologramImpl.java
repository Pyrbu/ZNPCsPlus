package lol.pyr.znpcsplus.hologram;

import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.Viewable;
import lol.pyr.znpcsplus.util.ZLocation;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HologramImpl extends Viewable implements Hologram {
    private final ConfigManager configManager;
    private final PacketFactory packetFactory;

    private double offset = 0.0;
    private ZLocation location;
    private final List<HologramLine> lines = new ArrayList<>();

    public HologramImpl(ConfigManager configManager, PacketFactory packetFactory, ZLocation location) {
        this.configManager = configManager;
        this.packetFactory = packetFactory;
        this.location = location;
    }

    public void addLine(Component line) {
        HologramLine newLine = new HologramLine(packetFactory, null, line);
        lines.add(newLine);
        relocateLines();
        for (Player viewer : getViewers()) newLine.show(viewer.getPlayer());
    }

    public Component getLine(int index) {
        return lines.get(index).getText();
    }

    public void removeLine(int index) {
        HologramLine line = lines.remove(index);
        for (Player viewer : getViewers()) line.hide(viewer);
        relocateLines();
    }

    public List<HologramLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public void clearLines() {
        UNSAFE_hideAll();
        lines.clear();
    }

    public void insertLine(int index, Component line) {
        HologramLine newLine = new HologramLine(packetFactory, null, line);
        lines.add(index, newLine);
        relocateLines();
        for (Player viewer : getViewers()) newLine.show(viewer.getPlayer());
    }

    @Override
    protected void UNSAFE_show(Player player) {
        for (HologramLine line : lines) line.show(player);
    }

    @Override
    protected void UNSAFE_hide(Player player) {
        for (HologramLine line : lines) line.hide(player);
    }

    public void setLocation(ZLocation location) {
        this.location = location;
        relocateLines();
    }

    private void relocateLines() {
        relocateLines(null);
    }

    private void relocateLines(HologramLine newLine) {
        final double lineSpacing = configManager.getConfig().lineSpacing();
        double height = location.getY() + (lines.size() - 1) * lineSpacing + getOffset();
        for (HologramLine line : lines) {
            line.setLocation(location.withY(height), line == newLine ? Collections.emptySet() : getViewers());
            height -= lineSpacing;
        }
    }

    public void setOffset(double offset) {
        this.offset = offset;
        relocateLines();
    }

    public double getOffset() {
        return offset;
    }
}
