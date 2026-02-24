package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.npc.NpcEntry;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.hologram.HologramItem;
import lol.pyr.znpcsplus.hologram.HologramLine;
import lol.pyr.znpcsplus.hologram.HologramText;
import lol.pyr.znpcsplus.interaction.ActionRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.scheduling.RepeatingTaskGuard;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.serialization.NpcSerializerRegistryImpl;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.storage.NpcStorageType;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.*;
import java.util.stream.Collectors;

public class NpcRegistryImpl implements NpcRegistry {
    private NpcStorage storage;
    private final PacketFactory packetFactory;
    private final ConfigManager configManager;
    private final LegacyComponentSerializer textSerializer;
    private final EntityPropertyRegistryImpl propertyRegistry;
    private final Object registryLock = new Object();

    private final List<NpcEntryImpl> npcList = new ArrayList<>();
    private final Map<String, NpcEntryImpl> npcIdLookupMap = new HashMap<>();
    private final Map<UUID, NpcEntryImpl> npcUuidLookupMap = new HashMap<>();

    public NpcRegistryImpl(ConfigManager configManager, ZNpcsPlus plugin, PacketFactory packetFactory, ActionRegistryImpl actionRegistry, TaskScheduler scheduler, NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, NpcSerializerRegistryImpl serializerRegistry, LegacyComponentSerializer textSerializer) {
        this.textSerializer = textSerializer;
        this.propertyRegistry = propertyRegistry;
        storage = configManager.getConfig().storageType().create(configManager, plugin, packetFactory, actionRegistry, typeRegistry, propertyRegistry, textSerializer, serializerRegistry);
        if (storage == null) {
            Bukkit.getLogger().warning("Failed to initialize storage, falling back to YAML");
            storage = NpcStorageType.YAML.create(configManager, plugin, packetFactory, actionRegistry, typeRegistry, propertyRegistry, textSerializer, serializerRegistry);
        }
        this.packetFactory = packetFactory;
        this.configManager = configManager;

        if (configManager.getConfig().autoSaveEnabled()) {
            long delay = configManager.getConfig().autoSaveInterval() * 20L;
            scheduler.runDelayedTimerAsync(RepeatingTaskGuard.wrap(Bukkit.getLogger(), "npc-autosave", this::save), delay, delay);
        }
    }

    @Override
    public void register(NpcEntry entry) {
        register((NpcEntryImpl) entry);
    }

    private void register(NpcEntryImpl entry) {
        if (entry == null) throw new NullPointerException();
        synchronized (registryLock) {
            NpcEntryImpl existingById = npcIdLookupMap.get(entry.getId());
            if (existingById != null && existingById != entry) unregisterInternal(existingById);

            NpcEntryImpl existingByUuid = npcUuidLookupMap.get(entry.getNpc().getUuid());
            if (existingByUuid != null && existingByUuid != entry) unregisterInternal(existingByUuid);

            npcIdLookupMap.put(entry.getId(), entry);
            npcUuidLookupMap.put(entry.getNpc().getUuid(), entry);
            if (!npcList.contains(entry)) npcList.add(entry);
        }
    }

    private void unregister(NpcEntryImpl entry) {
        synchronized (registryLock) {
            unregisterInternal(entry);
        }
    }

    private void unregisterInternal(NpcEntryImpl entry) {
        if (entry == null) return;
        npcList.remove(entry);
        npcIdLookupMap.entrySet().removeIf(mapEntry -> mapEntry.getValue() == entry);
        npcUuidLookupMap.entrySet().removeIf(mapEntry -> mapEntry.getValue() == entry);
        entry.getNpc().delete();
    }

    private void unregisterAll() {
        synchronized (registryLock) {
            for (NpcEntryImpl entry : npcList) {
                if (entry.isSave()) entry.getNpc().delete();
            }
            npcList.clear();
            npcIdLookupMap.clear();
            npcUuidLookupMap.clear();
        }
    }

    public void registerAll(Collection<NpcEntryImpl> entries) {
        for (NpcEntryImpl entry : entries) register(entry);
    }

    public void reload() {
        unregisterAll();
        registerAll(storage.loadNpcs());
    }

    public void save() {
        List<NpcEntryImpl> toSave;
        synchronized (registryLock) {
            toSave = npcList.stream().filter(NpcEntryImpl::isSave).collect(Collectors.toList());
        }
        storage.saveNpcs(toSave);
    }

    @Override
    public NpcEntryImpl getById(String id) {
        synchronized (registryLock) {
            return npcIdLookupMap.get(id.toLowerCase());
        }
    }

    @Override
    public NpcEntry getByUuid(UUID uuid) {
        synchronized (registryLock) {
            return npcUuidLookupMap.get(uuid);
        }
    }

    public Collection<NpcEntryImpl> getAll() {
        synchronized (registryLock) {
            return Collections.unmodifiableCollection(new ArrayList<>(npcList));
        }
    }

    public Collection<NpcEntryImpl> getProcessable() {
        synchronized (registryLock) {
            return Collections.unmodifiableCollection(npcList.stream()
                    .filter(NpcEntryImpl::isProcessed)
                    .collect(Collectors.toList()));
        }
    }

    public Collection<NpcEntryImpl> getAllModifiable() {
        synchronized (registryLock) {
            return Collections.unmodifiableCollection(npcList.stream()
                    .filter(NpcEntryImpl::isAllowCommandModification)
                    .collect(Collectors.toList()));
        }
    }

    public NpcEntryImpl getByEntityId(int id) {
        synchronized (registryLock) {
            return npcList.stream().filter(entry -> entry.getNpc().getEntity().getEntityId() == id ||
                            entry.getNpc().getHologram().getLines().stream().anyMatch(line -> line.getEntityId() == id))
                    .findFirst().orElse(null);
        }
    }

    public Collection<String> getAllIds() {
        synchronized (registryLock) {
            return Collections.unmodifiableSet(new HashSet<>(npcIdLookupMap.keySet()));
        }
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
        synchronized (registryLock) {
            return Collections.unmodifiableSet(npcIdLookupMap.entrySet().stream()
                    .filter(entry -> entry.getValue().isAllowCommandModification())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet()));
        }
    }

    public NpcEntryImpl create(String id, World world, NpcType type, NpcLocation location) {
        return create(id, world, (NpcTypeImpl) type, location);
    }

    public NpcEntryImpl create(String id, World world, NpcTypeImpl type, NpcLocation location) {
        id = id.toLowerCase();
        synchronized (registryLock) {
            if (npcIdLookupMap.containsKey(id)) throw new IllegalArgumentException("An npc with the id " + id + " already exists!");
            NpcImpl npc = new NpcImpl(UUID.randomUUID(), propertyRegistry, configManager, textSerializer, world, type, location, packetFactory);
            type.applyDefaultProperties(npc);
            NpcEntryImpl entry = new NpcEntryImpl(id, npc);
            register(entry);
            return entry;
        }
    }

    public NpcEntryImpl clone(String id, String newId, World newWorld, NpcLocation newLocation) {
        NpcEntryImpl oldNpc = getById(id);
        if (oldNpc == null) return null;
        NpcEntryImpl newNpc = create(newId, newWorld, oldNpc.getNpc().getType(), newLocation);
        newNpc.enableEverything();

        for (EntityProperty<?> property : oldNpc.getNpc().getAllProperties()) {
            newNpc.getNpc().UNSAFE_setProperty(property, oldNpc.getNpc().getProperty(property));
        }

        for (InteractionAction action : oldNpc.getNpc().getActions()) {
            newNpc.getNpc().addAction(action);
        }

        for (HologramLine<?> line : oldNpc.getNpc().getHologram().getLines()) {
            if (line instanceof HologramText) {
                HologramText text = (HologramText) line;
                newNpc.getNpc().getHologram().addTextLineComponent(text.getValue());
            }
            else if (line instanceof HologramItem) {
                HologramItem item = (HologramItem) line;
                newNpc.getNpc().getHologram().addItemLinePEStack(item.getValue());
            }
            else throw new IllegalArgumentException("Unknown hologram line type during clone");
        }

        return newNpc;
    }

    @Override
    public void delete(String id) {
        NpcEntryImpl entry;
        synchronized (registryLock) {
            entry = npcIdLookupMap.get(id.toLowerCase());
            if (entry == null) return;
            unregisterInternal(entry);
        }
        storage.deleteNpc(entry);
    }

    @Override
    public void delete(UUID uuid) {
        NpcEntryImpl entry;
        synchronized (registryLock) {
            entry = npcUuidLookupMap.get(uuid);
            if (entry == null) return;
            unregisterInternal(entry);
        }
        storage.deleteNpc(entry);
    }

    public void switchIds(String oldId, String newId) {
        String normalizedOldId = oldId.toLowerCase();
        String normalizedNewId = newId.toLowerCase();
        NpcEntryImpl oldEntry;
        NpcEntryImpl newEntry;
        synchronized (registryLock) {
            oldEntry = npcIdLookupMap.get(normalizedOldId);
            if (oldEntry == null) throw new IllegalArgumentException("No npc with id " + oldId + " exists!");
            if (npcIdLookupMap.containsKey(normalizedNewId))
                throw new IllegalArgumentException("An npc with the id " + normalizedNewId + " already exists!");

            npcIdLookupMap.remove(normalizedOldId);
            newEntry = new NpcEntryImpl(normalizedNewId, oldEntry.getNpc());
            newEntry.setSave(oldEntry.isSave());
            newEntry.setProcessed(oldEntry.isProcessed());
            newEntry.setAllowCommandModification(oldEntry.isAllowCommandModification());

            npcIdLookupMap.put(normalizedNewId, newEntry);
            npcUuidLookupMap.put(newEntry.getNpc().getUuid(), newEntry);
            int index = npcList.indexOf(oldEntry);
            if (index == -1) npcList.add(newEntry);
            else npcList.set(index, newEntry);
        }

        if (oldEntry.isSave()) {
            storage.deleteNpc(oldEntry);
            storage.saveNpcs(Collections.singletonList(newEntry));
        }
    }

    public void unload() {
        synchronized (registryLock) {
            npcList.forEach(npcEntry -> npcEntry.getNpc().delete());
            npcList.clear();
            npcIdLookupMap.clear();
            npcUuidLookupMap.clear();
        }
        storage.close();
    }

    public NpcStorage getStorage() {
        return storage;
    }
}
