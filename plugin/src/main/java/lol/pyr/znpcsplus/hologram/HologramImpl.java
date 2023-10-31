package lol.pyr.znpcsplus.hologram;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NpcLocation;
import lol.pyr.znpcsplus.util.Viewable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HologramImpl extends Viewable implements Hologram {
    private final ConfigManager configManager;
    private final PacketFactory packetFactory;
    private final LegacyComponentSerializer textSerializer;
    private final EntityPropertyRegistryImpl propertyRegistry;

    private double offset = 0.0;
    private long refreshDelay = -1;
    private long lastRefresh = System.currentTimeMillis();
    private NpcLocation location;
    private final List<HologramLine<?>> lines = new ArrayList<>();

    public HologramImpl(EntityPropertyRegistryImpl propertyRegistry, ConfigManager configManager, PacketFactory packetFactory, LegacyComponentSerializer textSerializer, NpcLocation location) {
        this.propertyRegistry = propertyRegistry;
        this.configManager = configManager;
        this.packetFactory = packetFactory;
        this.textSerializer = textSerializer;
        this.location = location;
    }

    public void addTextLineComponent(Component line) {
        HologramText newLine = new HologramText(propertyRegistry, packetFactory, null, line);
        lines.add(newLine);
        relocateLines(newLine);
        for (Player viewer : getViewers()) newLine.show(viewer.getPlayer());
    }

    public void addTextLine(String line) {
        try {
            addTextLineComponent(textSerializer.deserialize(textSerializer.serialize(MiniMessage.miniMessage().deserialize(line))));
        } catch (ParsingExceptionImpl e) {
            addTextLineComponent(Component.text(line));
        }
    }

    public void addItemLineStack(org.bukkit.inventory.ItemStack item) {
        addItemLinePEStack(SpigotConversionUtil.fromBukkitItemStack(item));
    }

    public void addItemLine(String serializedItem) {
        addItemLinePEStack(HologramItem.deserialize(serializedItem));
    }

    public void addItemLinePEStack(ItemStack item) {
        HologramItem newLine = new HologramItem(propertyRegistry, packetFactory, null, item);
        lines.add(newLine);
        relocateLines(newLine);
        for (Player viewer : getViewers()) newLine.show(viewer.getPlayer());
    }

    public void addLine(String line) {
        if (line.toLowerCase().startsWith("item:")) {
            addItemLine(line.substring(5));
        } else {
            addTextLine(line);
        }
    }

    public Component getLineTextComponent(int index) {
        return ((HologramText) lines.get(index)).getValue();
    }

    public String getLine(int index) {
        if (lines.get(index) instanceof HologramItem) {
            return ((HologramItem) lines.get(index)).serialize();
        } else {
            return textSerializer.serialize(getLineTextComponent(index));
        }
    }

    public void removeLine(int index) {
        HologramLine<?> line = lines.remove(index);
        for (Player viewer : getViewers()) line.hide(viewer);
        relocateLines();
    }

    public List<HologramLine<?>> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public void clearLines() {
        UNSAFE_hideAll();
        lines.clear();
    }

    public void insertTextLineComponent(int index, Component line) {
        HologramText newLine = new HologramText(propertyRegistry, packetFactory, null, line);
        lines.add(index, newLine);
        relocateLines(newLine);
        for (Player viewer : getViewers()) newLine.show(viewer.getPlayer());
    }

    public void insertTextLine(int index, String line) {
        insertTextLineComponent(index, textSerializer.deserialize(line));
    }

    public void insertItemLineStack(int index, org.bukkit.inventory.ItemStack item) {
        insertItemLinePEStack(index, SpigotConversionUtil.fromBukkitItemStack(item));
    }

    public void insertItemLinePEStack(int index, ItemStack item) {
        HologramItem newLine = new HologramItem(propertyRegistry, packetFactory, null, item);
        lines.add(index, newLine);
        relocateLines(newLine);
        for (Player viewer : getViewers()) newLine.show(viewer.getPlayer());
    }

    public void insertItemLine(int index, String item) {
        insertItemLinePEStack(index, HologramItem.deserialize(item));
    }

    public void insertLine(int index, String line) {
        if (line.toLowerCase().startsWith("item:")) {
            insertItemLine(index, line.substring(5));
        } else {
            insertTextLine(index, line);
        }
    }

    @Override
    public int lineCount() {
        return lines.size();
    }

    @Override
    protected void UNSAFE_show(Player player) {
        for (HologramLine<?> line : lines) line.show(player);
    }

    @Override
    protected void UNSAFE_hide(Player player) {
        for (HologramLine<?> line : lines) line.hide(player);
    }

    public long getRefreshDelay() {
        return refreshDelay;
    }

    public void setRefreshDelay(long refreshDelay) {
        this.refreshDelay = refreshDelay;
    }

    public boolean shouldRefresh() {
        return refreshDelay != -1 && (System.currentTimeMillis() - lastRefresh) > refreshDelay;
    }

    public void refresh() {
        lastRefresh = System.currentTimeMillis();
        for (HologramLine<?> line : lines) for (Player viewer : getViewers()) line.refreshMeta(viewer);
    }

    public void setLocation(NpcLocation location) {
        this.location = location;
        relocateLines();
    }

    private void relocateLines() {
        relocateLines(null);
    }

    private void relocateLines(HologramLine<?> newLine) {
        final double lineSpacing = configManager.getConfig().lineSpacing();
        double height = location.getY() + (lines.size() - 1) * lineSpacing + getOffset();
        for (HologramLine<?> line : lines) {
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
