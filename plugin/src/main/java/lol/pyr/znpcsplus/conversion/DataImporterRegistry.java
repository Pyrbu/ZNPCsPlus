package lol.pyr.znpcsplus.conversion;

import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.conversion.znpcs.ZNpcImporter;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import lol.pyr.znpcsplus.util.BungeeConnector;
import lol.pyr.znpcsplus.util.LazyLoader;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataImporterRegistry {
    private final Map<String, LazyLoader<DataImporter>> importers = new HashMap<>();

    public DataImporterRegistry(ConfigManager configManager, BukkitAudiences adventure, BungeeConnector bungeeConnector,
                                TaskScheduler taskScheduler, PacketFactory packetFactory, LegacyComponentSerializer textSerializer,
                                NpcTypeRegistryImpl typeRegistry, File pluginsFolder, EntityPropertyRegistryImpl propertyRegistry,
                                SkinCache skinCache) {

        register("znpcs", LazyLoader.of(() -> new ZNpcImporter(configManager, adventure, bungeeConnector, taskScheduler,
                packetFactory, textSerializer, typeRegistry, propertyRegistry, skinCache, new File(pluginsFolder, "ServersNPC/data.json"))));
    }

    private void register(String id, LazyLoader<DataImporter> loader) {
        importers.put(id.toUpperCase(), loader);
    }

    public DataImporter getImporter(String id) {
        id = id.toUpperCase();
        return importers.containsKey(id) ? importers.get(id).get() : null;
    }

    public Collection<String> getIds() {
        return Collections.unmodifiableSet(importers.keySet());
    }
}
