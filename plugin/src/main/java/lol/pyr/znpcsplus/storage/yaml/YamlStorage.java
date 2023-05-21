package lol.pyr.znpcsplus.storage.yaml;

import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.hologram.HologramLine;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class YamlStorage implements NpcStorage {
    private final PacketFactory packetFactory;
    private final ConfigManager configManager;
    private final ActionRegistry actionRegistry;
    private final NpcTypeRegistryImpl typeRegistry;
    private final EntityPropertyRegistryImpl propertyRegistry;
    private final File folder;

    public YamlStorage(PacketFactory packetFactory, ConfigManager configManager, ActionRegistry actionRegistry, NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, File folder) {
        this.packetFactory = packetFactory;
        this.configManager = configManager;
        this.actionRegistry = actionRegistry;
        this.typeRegistry = typeRegistry;
        this.propertyRegistry = propertyRegistry;
        this.folder = folder;
        if (!this.folder.exists()) this.folder.mkdirs();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Collection<NpcEntryImpl> loadNpcs() {
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) return Collections.emptyList();
        List<NpcEntryImpl> npcs = new ArrayList<>();
        for (File file : files) if (file.isFile() && file.getName().toLowerCase().endsWith(".yml")) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            NpcImpl npc = new NpcImpl(configManager, packetFactory, config.getString("world"), typeRegistry.getByName(config.getString("type")),
                    deserializeLocation(config.getConfigurationSection("location")));

            ConfigurationSection properties = config.getConfigurationSection("properties");
            if (properties != null) {
                for (String key : properties.getKeys(false)) {
                    EntityPropertyImpl<?> property = propertyRegistry.getByName(key);
                    npc.UNSAFE_setProperty(property, property.deserialize(properties.getString(key)));
                }
            }
            npc.getHologram().setOffset(config.getDouble("hologram.offset", 0.0));
            for (String line : config.getStringList("hologram.lines")) npc.getHologram().addLine(MiniMessage.miniMessage().deserialize(line));
            for (String s : config.getStringList("actions")) npc.addAction(actionRegistry.deserialize(s));

            NpcEntryImpl entry = new NpcEntryImpl(config.getString("id"), npc);
            entry.setProcessed(config.getBoolean("is-processed"));
            entry.setAllowCommandModification(config.getBoolean("allow-commands"));
            entry.setSave(true);

            npcs.add(entry);
        }
        return npcs;
    }

    @Override
    public void saveNpcs(Collection<NpcEntryImpl> npcs) {
        File[] files = folder.listFiles();
        if (files != null && files.length != 0) for (File file : files) file.delete();
        for (NpcEntryImpl entry : npcs) try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("id", entry.getId());
            config.set("is-processed", entry.isProcessed());
            config.set("allow-commands", entry.isAllowCommandModification());

            NpcImpl npc = entry.getNpc();
            config.set("world", npc.getWorldName());
            config.set("location", serializeLocation(npc.getLocation()));
            config.set("type", npc.getType().getName());

            for (EntityPropertyImpl<?> property : npc.getAppliedProperties()) {
                config.set("properties." + property.getName(), property.serialize(npc));
            }

            if (npc.getHologram().getOffset() != 0.0) config.set("hologram.offset", npc.getHologram().getOffset());
            List<String> lines = new ArrayList<>();
            for (HologramLine line : npc.getHologram().getLines()) {
                lines.add(MiniMessage.miniMessage().serialize(line.getText()));
            }
            config.set("hologram.lines", lines);
            config.set("actions", npc.getActions().stream()
                    .map(actionRegistry::serialize)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

            config.save(new File(folder, entry.getId() + ".yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NpcLocation deserializeLocation(ConfigurationSection section) {
        return new NpcLocation(
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
    }

    public YamlConfiguration serializeLocation(NpcLocation location) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("x", location.getX());
        config.set("y", location.getY());
        config.set("z", location.getZ());
        config.set("yaw", location.getYaw());
        config.set("pitch", location.getPitch());
        return config;
    }
}
