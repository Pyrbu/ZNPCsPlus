package lol.pyr.znpcsplus.conversion.citizens;

import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.conversion.DataImporter;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.util.BungeeConnector;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("FieldCanBeLocal")
public class CitizensImporter implements DataImporter {
    private final ConfigManager configManager;
    private final BukkitAudiences adventure;
    private final BungeeConnector bungeeConnector;
    private final TaskScheduler scheduler;
    private final PacketFactory packetFactory;
    private final LegacyComponentSerializer textSerializer;
    private final NpcTypeRegistryImpl typeRegistry;
    private final EntityPropertyRegistryImpl propertyRegistry;
    private final MojangSkinCache skinCache;
    private final File dataFile;

    public CitizensImporter(ConfigManager configManager, BukkitAudiences adventure, BungeeConnector bungeeConnector,
                            TaskScheduler taskScheduler, PacketFactory packetFactory, LegacyComponentSerializer textSerializer,
                            NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, MojangSkinCache skinCache,
                            File dataFile) {
        this.configManager = configManager;
        this.adventure = adventure;
        this.bungeeConnector = bungeeConnector;
        this.scheduler = taskScheduler;
        this.packetFactory = packetFactory;
        this.textSerializer = textSerializer;
        this.typeRegistry = typeRegistry;
        this.propertyRegistry = propertyRegistry;
        this.skinCache = skinCache;
        this.dataFile = dataFile;
    }

    @Override
    public Collection<NpcEntryImpl> importData() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        // TODO
        return Collections.emptyList();
    }

    @Override
    public boolean isValid() {
        return dataFile.isFile();
    }
}
