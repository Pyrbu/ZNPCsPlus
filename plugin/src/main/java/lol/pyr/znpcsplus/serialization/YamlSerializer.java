package lol.pyr.znpcsplus.serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import lol.pyr.znpcsplus.api.serialization.NpcSerializer;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlSerializer implements NpcSerializer<YamlConfiguration> {
  private static final Logger logger = Logger.getLogger("YamlSerializer");

  private final PacketFactory packetFactory;
  private final ConfigManager configManager;
  private final ActionRegistryImpl actionRegistry;
  private final NpcTypeRegistryImpl typeRegistry;
  private final EntityPropertyRegistryImpl propertyRegistry;
  private final LegacyComponentSerializer textSerializer;

  public YamlSerializer(
      PacketFactory packetFactory,
      ConfigManager configManager,
      ActionRegistryImpl actionRegistry,
      NpcTypeRegistryImpl typeRegistry,
      EntityPropertyRegistryImpl propertyRegistry,
      LegacyComponentSerializer textSerializer) {
    this.packetFactory = packetFactory;
    this.configManager = configManager;
    this.actionRegistry = actionRegistry;
    this.typeRegistry = typeRegistry;
    this.propertyRegistry = propertyRegistry;
    this.textSerializer = textSerializer;
  }

  @Override
  public YamlConfiguration serialize(NpcEntry entry) {
    YamlConfiguration config = new YamlConfiguration();
    config.set("id", entry.getId());
    config.set("is-processed", entry.isProcessed());
    config.set("allow-commands", entry.isAllowCommandModification());
    config.set("save", entry.isSave());

    NpcImpl npc = (NpcImpl) entry.getNpc();
    config.set("enabled", npc.isEnabled());
    config.set("uuid", npc.getUuid().toString());
    config.set("world", npc.getWorldName());
    config.set("location", serializeLocation(npc.getLocation()));
    config.set("type", npc.getType().getName());

    for (EntityProperty<?> property : npc.getAllProperties())
      try {
        PropertySerializer<?> serializer =
            propertyRegistry.getSerializer(((EntityPropertyImpl<?>) property).getType());
        if (serializer == null) {
          Bukkit.getLogger()
              .log(
                  Level.WARNING,
                  "Unknown serializer for property '"
                      + property.getName()
                      + "' for npc '"
                      + entry.getId()
                      + "'. skipping ...");
          continue;
        }
        config.set(
            "properties." + property.getName(),
            serializer.UNSAFE_serialize(npc.getProperty(property)));
      } catch (Exception exception) {
        logger.severe(
            "Failed to serialize property "
                + property.getName()
                + " for npc with id "
                + entry.getId());
        exception.printStackTrace();
      }

    HologramImpl hologram = npc.getHologram();
    if (hologram.getOffset() != 0.0) config.set("hologram.offset", hologram.getOffset());
    if (hologram.getRefreshDelay() != -1)
      config.set("hologram.refresh-delay", hologram.getRefreshDelay());
    List<String> lines = new ArrayList<>(npc.getHologram().getLines().size());
    for (int i = 0; i < hologram.getLines().size(); i++) {
      lines.add(hologram.getLine(i));
    }
    config.set("hologram.lines", lines);
    config.set(
        "actions",
        npc.getActions().stream()
            .map(actionRegistry::serialize)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));

    return config;
  }

  @Override
  public NpcEntry deserialize(YamlConfiguration config) {
    UUID uuid =
        config.contains("uuid") ? UUID.fromString(config.getString("uuid")) : UUID.randomUUID();
    NpcImpl npc =
        new NpcImpl(
            uuid,
            propertyRegistry,
            configManager,
            packetFactory,
            textSerializer,
            config.getString("world"),
            typeRegistry.getByName(config.getString("type")),
            deserializeLocation(config.getConfigurationSection("location")));

    if (config.isBoolean("enabled")) npc.setEnabled(config.getBoolean("enabled"));

    ConfigurationSection properties = config.getConfigurationSection("properties");
    if (properties != null) {
      for (String key : properties.getKeys(false)) {
        EntityPropertyImpl<?> property = propertyRegistry.getByName(key);
        if (property == null) {
          Bukkit.getLogger()
              .log(
                  Level.WARNING,
                  "Unknown property '"
                      + key
                      + "' for npc '"
                      + config.getString("id")
                      + "'. skipping ...");
          continue;
        }
        PropertySerializer<?> serializer = propertyRegistry.getSerializer(property.getType());
        if (serializer == null) {
          Bukkit.getLogger()
              .log(
                  Level.WARNING,
                  "Unknown serializer for property '"
                      + key
                      + "' for npc '"
                      + config.getString("id")
                      + "'. skipping ...");
          continue;
        }
        Object value = serializer.deserialize(properties.getString(key));
        if (value == null) {
          Bukkit.getLogger()
              .log(
                  Level.WARNING,
                  "Failed to deserialize property '"
                      + key
                      + "' for npc '"
                      + config.getString("id")
                      + "'. Resetting to default ...");
          value = property.getDefaultValue();
        }
        npc.UNSAFE_setProperty(property, value);
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
    entry.setSave(config.getBoolean("save", true));

    return entry;
  }

  public NpcLocation deserializeLocation(ConfigurationSection section) {
    return new NpcLocation(
        section.getDouble("x"),
        section.getDouble("y"),
        section.getDouble("z"),
        (float) section.getDouble("yaw"),
        (float) section.getDouble("pitch"));
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
