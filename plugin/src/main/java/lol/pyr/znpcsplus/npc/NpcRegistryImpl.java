package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.World;

import java.util.*;
import java.util.stream.Collectors;

public class NpcRegistryImpl implements NpcRegistry {
    private final NpcStorage storage;
    private final PacketFactory packetFactory;
    private final ConfigManager configManager;
    private final LegacyComponentSerializer textSerializer;

    private final List<NpcEntryImpl> npcList = new ArrayList<>();
    private final Map<String, NpcEntryImpl> npcIdLookupMap = new HashMap<>();
    private final Map<UUID, NpcEntryImpl> npcUuidLookupMap = new HashMap<>();

    public NpcRegistryImpl(ConfigManager configManager, ZNpcsPlus plugin, PacketFactory packetFactory, ActionRegistry actionRegistry, TaskScheduler scheduler, NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer) {
        this.textSerializer = textSerializer;
        storage = configManager.getConfig().storageType().create(configManager, plugin, packetFactory, actionRegistry, typeRegistry, propertyRegistry, textSerializer);
        this.packetFactory = packetFactory;
        this.configManager = configManager;

        if (configManager.getConfig().autoSaveEnabled()) {
            long delay = configManager.getConfig().autoSaveInterval() * 20L;
            scheduler.runDelayedTimerAsync(this::save, delay, delay);
        }
    }

    private void register(NpcEntryImpl entry) {
        unregister(npcIdLookupMap.put(entry.getId(), entry));
        unregister(npcUuidLookupMap.put(entry.getNpc().getUuid(), entry));
        npcList.add(entry);
    }

    private void unregister(NpcEntryImpl entry) {
        if (entry == null) return;
        npcList.remove(entry);
        NpcImpl one = npcIdLookupMap.remove(entry.getId()).getNpc();
        NpcImpl two = npcUuidLookupMap.remove(entry.getNpc().getUuid()).getNpc();
        if (one != null) one.delete();
        if (two != null && !Objects.equals(one, two)) two.delete();
    }

    private void unregisterAll() {
        for (NpcEntryImpl entry : npcList) entry.getNpc().delete();
        npcList.clear();
        npcIdLookupMap.clear();
        npcUuidLookupMap.clear();
    }

    public void registerAll(Collection<NpcEntryImpl> entries) {
        for (NpcEntryImpl entry : entries) register(entry);
    }

    public void reload() {
        unregisterAll();
        registerAll(storage.loadNpcs());
    }

    public void save() {
        storage.saveNpcs(npcList.stream().filter(NpcEntryImpl::isSave).collect(Collectors.toList()));
    }

    @Override
    public NpcEntryImpl getById(String id) {
        return npcIdLookupMap.get(id.toLowerCase());
    }

    @Override
    public NpcEntry getByUuid(UUID uuid) {
        return npcUuidLookupMap.get(uuid);
    }

    public Collection<NpcEntryImpl> getAll() {
        return Collections.unmodifiableCollection(npcList);
    }

    public Collection<NpcEntryImpl> getProcessable() {
        return Collections.unmodifiableCollection(npcList.stream()
                .filter(NpcEntryImpl::isProcessed)
                .collect(Collectors.toList()));
    }

    public Collection<NpcEntryImpl> getAllModifiable() {
        return Collections.unmodifiableCollection(npcList.stream()
                .filter(NpcEntryImpl::isAllowCommandModification)
                .collect(Collectors.toList()));
    }

    public NpcEntryImpl getByEntityId(int id) {
        return npcList.stream().filter(entry -> entry.getNpc().getEntity().getEntityId() == id).findFirst().orElse(null);
    }

    public Collection<String> getAllIds() {
        return Collections.unmodifiableSet(npcIdLookupMap.keySet());
    }

    @Override
    public Collection<? extends NpcEntry> getAllPlayerMade() {
        return getAllModifiable();
    }

    @Override
    public Collection<String> getAllPlayerMadeIds() {
        return getAllModifiable().stream()
                .map(NpcEntryImpl::getId)
                .collect(Collectors.toSet());
    }

    public Collection<String> getModifiableIds() {
        return Collections.unmodifiableSet(npcIdLookupMap.entrySet().stream()
                .filter(entry -> entry.getValue().isAllowCommandModification())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet()));
    }

    public NpcEntryImpl create(String id, World world, NpcType type, NpcLocation location) {
        return create(id, world, (NpcTypeImpl) type, location);
    }

    public NpcEntryImpl create(String id, World world, NpcTypeImpl type, NpcLocation location) {
        id = id.toLowerCase();
        if (npcIdLookupMap.containsKey(id)) throw new IllegalArgumentException("An npc with the id " + id + " already exists!");
        NpcImpl npc = new NpcImpl(UUID.randomUUID(), configManager, textSerializer, world, type, location, packetFactory);
        NpcEntryImpl entry = new NpcEntryImpl(id, npc);
        npcIdLookupMap.put(id, entry);
        return entry;
    }

    @Override
    public void delete(String id) {
        id = id.toLowerCase();
        if (!npcIdLookupMap.containsKey(id)) return;
        npcIdLookupMap.remove(id).getNpc().delete();
    }
}
