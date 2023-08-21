package lol.pyr.znpcsplus.storage.yaml;

import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class YamlStorage implements NpcStorage {
    private final PacketFactory packetFactory;
    private final ConfigManager configManager;
    private final ActionRegistry actionRegistry;
    private final NpcTypeRegistryImpl typeRegistry;
    private final EntityPropertyRegistryImpl propertyRegistry;
    private final LegacyComponentSerializer textSerializer;
    private final File folder;

    public YamlStorage(PacketFactory packetFactory, ConfigManager configManager, ActionRegistry actionRegistry, NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer, File folder) {
        this.packetFactory = packetFactory;
        this.configManager = configManager;
        this.actionRegistry = actionRegistry;
        this.typeRegistry = typeRegistry;
        this.propertyRegistry = propertyRegistry;
        this.textSerializer = textSerializer;
        this.folder = folder;
        if (!this.folder.exists()) this.folder.mkdirs();
    }

    @Override
    public Collection<NpcEntryImpl> loadNpcs() {
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) return Collections.emptyList();
        List<NpcEntryImpl> npcs = new ArrayList<>(files.length);
        for (File file : files) if (file.isFile() && file.getName().toLowerCase().endsWith(".yml")) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            UUID uuid = config.contains("uuid") ? UUID.fromString(config.getString("uuid")) : UUID.randomUUID();
            NpcImpl npc = new NpcImpl(uuid, propertyRegistry, configManager, packetFactory, textSerializer, config.getString("world"),
                    typeRegistry.getByName(config.getString("type")), deserializeLocation(config.getConfigurationSection("location")));

            if (config.isBoolean("enabled")) npc.setEnabled(config.getBoolean("enabled"));

            ConfigurationSection properties = config.getConfigurationSection("properties");
            if (properties != null) {
                for (String key : properties.getKeys(false)) {
                    EntityPropertyImpl<?> property = propertyRegistry.getByName(key);
                    if (property == null) {
                        Bukkit.getLogger().log(Level.WARNING, "Unknown property '" + key + "' for npc '" + config.getString("id") + "'. skipping ...");
                        continue;
                    }
                    PropertySerializer<?> serializer = propertyRegistry.getSerializer(property.getType());
                    if (serializer == null) {
                        Bukkit.getLogger().log(Level.WARNING, "Unknown serializer for property '" + key + "' for npc '" + config.getString("id") + "'. skipping ...");
                        continue;
                    }
                    npc.UNSAFE_setProperty(property, serializer.deserialize(properties.getString(key)));
                }
            }
            HologramImpl hologram = npc.getHologram();
            hologram.setOffset(config.getDouble("hologram.offset", 0.0));
            hologram.setRefreshDelay(config.getLong("hologram.refresh-delay", -1));
            for (String line : config.getStringList("hologram.lines")) hologram.addLine(line);
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
            config.set("enabled", npc.isEnabled());
            config.set("uuid", npc.getUuid().toString());
            config.set("world", npc.getWorldName());
            config.set("location", serializeLocation(npc.getLocation()));
            config.set("type", npc.getType().getName());

            for (EntityProperty<?> property : npc.getAppliedProperties()) {
                PropertySerializer<?> serializer = propertyRegistry.getSerializer(((EntityPropertyImpl<?>) property).getType());
                config.set("properties." + property.getName(), serializer.UNSAFE_serialize(npc.getProperty(property)));
            }

            HologramImpl hologram = npc.getHologram();
            if (hologram.getOffset() != 0.0) config.set("hologram.offset", hologram.getOffset());
            if (hologram.getRefreshDelay() != -1) config.set("hologram.refresh-delay", hologram.getRefreshDelay());
            List<String> lines = new ArrayList<>(npc.getHologram().getLines().size());
            for (int i = 0; i < hologram.getLines().size(); i++) {
                lines.add(hologram.getLine(i));
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
