package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.World;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NpcRegistryImpl implements NpcRegistry {
    private final NpcStorage storage;
    private final PacketFactory packetFactory;
    private final ConfigManager configManager;

    public NpcRegistryImpl(ConfigManager configManager, ZNpcsPlus plugin, PacketFactory packetFactory, ActionRegistry actionRegistry, TaskScheduler scheduler, NpcTypeRegistry typeRegistry, EntityPropertyRegistry propertyRegistry) {
        storage = configManager.getConfig().storageType().create(configManager, plugin, packetFactory, actionRegistry, typeRegistry, propertyRegistry);
        this.packetFactory = packetFactory;
        this.configManager = configManager;

        if (configManager.getConfig().autoSaveEnabled()) {
            long delay = configManager.getConfig().autoSaveInterval() * 20L;
            scheduler.runDelayedTimerAsync(this::save, delay, delay);
        }
    }

    public void reload() {
        npcMap.clear();
        for (NpcEntryImpl entry : storage.loadNpcs()) npcMap.put(entry.getId(), entry);
    }

    public void save() {
        storage.saveNpcs(npcMap.values().stream().filter(NpcEntryImpl::isSave).collect(Collectors.toList()));
    }

    private final Map<String, NpcEntryImpl> npcMap = new HashMap<>();

    public NpcEntryImpl get(String id) {
        return npcMap.get(id.toLowerCase());
    }

    public Collection<NpcEntryImpl> getAll() {
        return Collections.unmodifiableCollection(npcMap.values());
    }

    public Collection<NpcEntryImpl> getAllModifiable() {
        return Collections.unmodifiableCollection(npcMap.values().stream()
                .filter(NpcEntryImpl::isAllowCommandModification)
                .collect(Collectors.toList()));
    }

    public NpcEntryImpl getByEntityId(int id) {
        return getAll().stream().filter(entry -> entry.getNpc().getEntity().getEntityId() == id).findFirst().orElse(null);
    }

    public Collection<String> getIds() {
        return Collections.unmodifiableSet(npcMap.keySet());
    }

    public Collection<String> getModifiableIds() {
        return Collections.unmodifiableSet(npcMap.entrySet().stream()
                .filter(entry -> entry.getValue().isAllowCommandModification())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet()));
    }

    public NpcEntryImpl create(String id, World world, NpcType type, NpcLocation location) {
        return create(id, world, (NpcTypeImpl) type, location);
    }

    public NpcEntryImpl create(String id, World world, NpcTypeImpl type, NpcLocation location) {
        id = id.toLowerCase();
        if (npcMap.containsKey(id)) throw new IllegalArgumentException("An npc with the id " + id + " already exists!");
        NpcImpl npc = new NpcImpl(configManager, world, type, location, packetFactory);
        NpcEntryImpl entry = new NpcEntryImpl(id, npc);
        npcMap.put(id, entry);
        return entry;
    }

    @Override
    public void delete(String id) {
        id = id.toLowerCase();
        if (!npcMap.containsKey(id)) return;
        npcMap.remove(id).getNpc().delete();
    }
}
