package lol.pyr.znpcsplus.storage.yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import lol.pyr.znpcsplus.api.serialization.NpcSerializer;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.serialization.NpcSerializerRegistryImpl;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlStorage implements NpcStorage {
  private static final Logger logger = Logger.getLogger("YamlStorage");

  private final File folder;
  private final NpcSerializer<YamlConfiguration> yamlSerializer;

  public YamlStorage(NpcSerializerRegistryImpl serializerRegistry, File folder) {
    this.yamlSerializer = serializerRegistry.getSerializer(YamlConfiguration.class);
    this.folder = folder;
    if (!this.folder.exists()) this.folder.mkdirs();
  }

  @Override
  public Collection<NpcEntryImpl> loadNpcs() {
    File[] files = folder.listFiles();
    if (files == null || files.length == 0) return Collections.emptyList();
    List<NpcEntryImpl> npcs = new ArrayList<>(files.length);
    for (File file : files)
      if (file.isFile() && file.getName().toLowerCase().endsWith(".yml"))
        try {
          YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
          npcs.add((NpcEntryImpl) yamlSerializer.deserialize(config));
        } catch (Throwable t) {
          logger.severe("Failed to load npc file: " + file.getName());
          t.printStackTrace();
        }
    return npcs;
  }

  @Override
  public void saveNpcs(Collection<NpcEntryImpl> npcs) {
    for (NpcEntryImpl entry : npcs)
      try {
        YamlConfiguration config = yamlSerializer.serialize(entry);
        config.save(fileFor(entry));
      } catch (Exception exception) {
        logger.severe("Failed to save npc with id " + entry.getId());
        exception.printStackTrace();
      }
  }

  @Override
  public void deleteNpc(NpcEntryImpl npc) {
    fileFor(npc).delete();
  }

  private File fileFor(NpcEntryImpl entry) {
    return new File(folder, entry.getId() + ".yml");
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
