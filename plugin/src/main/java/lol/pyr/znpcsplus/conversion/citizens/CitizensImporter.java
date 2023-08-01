package lol.pyr.znpcsplus.conversion.citizens;

import lol.pyr.znpcsplus.api.NpcApiProvider;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.conversion.DataImporter;
import lol.pyr.znpcsplus.conversion.citizens.model.CitizensTrait;
import lol.pyr.znpcsplus.conversion.citizens.model.CitizensTraitsRegistry;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.util.BungeeConnector;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

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
    private final CitizensTraitsRegistry traitsRegistry;

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
        this.traitsRegistry = new CitizensTraitsRegistry(typeRegistry, propertyRegistry, skinCache);
    }

    @Override
    public Collection<NpcEntryImpl> importData() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection npcsSection = config.getConfigurationSection("npc");
        // use guard clause to avoid nested if statements
        if (npcsSection == null) {
            return Collections.emptyList();
        }
        ArrayList<NpcEntryImpl> entries = new ArrayList<>();
        npcsSection.getKeys(false).forEach(key -> {
            ConfigurationSection npcSection = npcsSection.getConfigurationSection(key);
            if (npcSection == null) {
                return;
            }
            String name = npcSection.getString("name", "Citizens NPC");
            UUID uuid;
            try {
                uuid = UUID.fromString(npcSection.getString("uuid"));
            } catch (IllegalArgumentException e) {
                uuid = UUID.randomUUID();
            }
            String world = npcSection.getString("traits.location.world");
            if (world == null) {
                world = Bukkit.getWorlds().get(0).getName();
            }
            NpcImpl npc = new NpcImpl(uuid, propertyRegistry, configManager, packetFactory, textSerializer, world, typeRegistry.getByName("armor_stand"), new NpcLocation(0, 0, 0, 0, 0));
            npc.getHologram().addLineComponent(textSerializer.deserialize(name));
            ConfigurationSection traits = npcSection.getConfigurationSection("traits");
            if (traits != null) {
                for (String traitName : traits.getKeys(false)) {
                    Object trait = traits.get(traitName);
                    CitizensTrait citizensTrait = traitsRegistry.getByName(traitName);
                    if (citizensTrait != null) {
                        npc = citizensTrait.apply(npc, trait);
                    }
                }
            }
            String id = key.toLowerCase();
            while (NpcApiProvider.get().getNpcRegistry().getById(id) != null) {
                id += "_";
            }
            NpcEntryImpl entry = new NpcEntryImpl(id, npc);
            entry.enableEverything();
            entries.add(entry);
        });
        return entries;
    }

    @Override
    public boolean isValid() {
        return dataFile.isFile();
    }
}
