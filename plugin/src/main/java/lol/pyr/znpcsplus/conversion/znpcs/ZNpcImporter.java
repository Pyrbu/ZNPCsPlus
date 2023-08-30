package lol.pyr.znpcsplus.conversion.znpcs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lol.pyr.znpcsplus.api.NpcApiProvider;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.conversion.DataImporter;
import lol.pyr.znpcsplus.conversion.znpcs.model.ZNpcsAction;
import lol.pyr.znpcsplus.conversion.znpcs.model.ZNpcsLocation;
import lol.pyr.znpcsplus.conversion.znpcs.model.ZNpcsModel;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.interaction.InteractionActionImpl;
import lol.pyr.znpcsplus.interaction.consolecommand.ConsoleCommandAction;
import lol.pyr.znpcsplus.interaction.message.MessageAction;
import lol.pyr.znpcsplus.interaction.playerchat.PlayerChatAction;
import lol.pyr.znpcsplus.interaction.playercommand.PlayerCommandAction;
import lol.pyr.znpcsplus.interaction.switchserver.SwitchServerAction;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.Skin;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.skin.descriptor.FetchingDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import lol.pyr.znpcsplus.util.ItemSerializationUtil;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ZNpcImporter implements DataImporter {
    private final ConfigManager configManager;
    private final BukkitAudiences adventure;
    private final TaskScheduler taskScheduler;
    private final PacketFactory packetFactory;
    private final LegacyComponentSerializer textSerializer;
    private final NpcTypeRegistryImpl typeRegistry;
    private final EntityPropertyRegistryImpl propertyRegistry;
    private final MojangSkinCache skinCache;
    private final File dataFile;
    private final Gson gson;

    public ZNpcImporter(ConfigManager configManager, BukkitAudiences adventure,
                        TaskScheduler taskScheduler, PacketFactory packetFactory, LegacyComponentSerializer textSerializer,
                        NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, MojangSkinCache skinCache, File dataFile) {

        this.configManager = configManager;
        this.adventure = adventure;
        this.taskScheduler = taskScheduler;
        this.packetFactory = packetFactory;
        this.textSerializer = textSerializer;
        this.typeRegistry = typeRegistry;
        this.propertyRegistry = propertyRegistry;
        this.skinCache = skinCache;
        this.dataFile = dataFile;
        gson = new GsonBuilder()
                .create();
    }

    @Override
    public Collection<NpcEntryImpl> importData() {
        ZNpcsModel[] models;
        try (BufferedReader fileReader = Files.newBufferedReader(dataFile.toPath())) {
            JsonElement element = JsonParser.parseReader(fileReader);
            models = gson.fromJson(element, ZNpcsModel[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        if (models == null) return Collections.emptyList();
        ArrayList<NpcEntryImpl> entries = new ArrayList<>(models.length);
        for (ZNpcsModel model : models) {
            String type = model.getNpcType();

            switch (type.toLowerCase()) {
                case "mushroom_cow":
                    type = "mooshroom";
                    break;
                case "snowman":
                    type = "snow_golem";
                    break;
            }

            ZNpcsLocation oldLoc = model.getLocation();
            NpcLocation location = new NpcLocation(oldLoc.getX(), oldLoc.getY(), oldLoc.getZ(), oldLoc.getYaw(), oldLoc.getPitch());
            UUID uuid = model.getUuid() == null ? UUID.randomUUID() : model.getUuid();
            NpcImpl npc = new NpcImpl(uuid, configManager, packetFactory, textSerializer, oldLoc.getWorld(), typeRegistry.getByName(type), location);

            HologramImpl hologram = npc.getHologram();
            hologram.setOffset(model.getHologramHeight());
            for (String raw : model.getHologramLines()) {
                Component line = textSerializer.deserialize(raw);
                hologram.addLineComponent(line);
            }

            for (ZNpcsAction action : model.getClickActions()) {
                InteractionType t = adaptClickType(action.getClickType());
                npc.addAction(adaptAction(action.getActionType(), t, action.getAction(), action.getDelay()));
            }

            for (Map.Entry<String, String> entry : model.getNpcEquip().entrySet()) {
                EntityPropertyImpl<ItemStack> property = propertyRegistry.getByName(entry.getKey(), ItemStack.class);
                if (property == null) continue;
                npc.setProperty(property, ItemSerializationUtil.itemFromB64(entry.getValue()));
            }

            if (model.getSkinName() != null) {
                npc.setProperty(propertyRegistry.getByName("skin", SkinDescriptor.class), new FetchingDescriptor(skinCache, model.getSkinName()));
            }
            else if (model.getSkin() != null && model.getSignature() != null) {
                npc.setProperty(propertyRegistry.getByName("skin", SkinDescriptor.class), new PrefetchedDescriptor(new Skin(model.getSkin(), model.getSignature())));
            }

            String id = String.valueOf(model.getId());

            while (NpcApiProvider.get().getNpcRegistry().getById(id) != null) {
                id += "_";
            }

            NpcEntryImpl entry = new NpcEntryImpl(id, npc);
            entry.enableEverything();
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public boolean isValid() {
        return dataFile.isFile();
    }

    private InteractionType adaptClickType(String clickType) {
        switch (clickType.toLowerCase()) {
            case "default":
                return InteractionType.ANY_CLICK;
            case "left":
                return InteractionType.LEFT_CLICK;
            case "right":
                return InteractionType.RIGHT_CLICK;
        }
        throw new IllegalArgumentException("Couldn't adapt znpcs click type: " + clickType);
    }

    private InteractionActionImpl adaptAction(String type, InteractionType clickType, String parameter, int cooldown) {
        switch (type.toLowerCase()) {
            case "cmd":
                return new PlayerCommandAction(taskScheduler, parameter, clickType, cooldown * 1000L, 0);
            case "console":
                return new ConsoleCommandAction(taskScheduler, parameter, clickType, cooldown * 1000L, 0);
            case "chat":
                return new PlayerChatAction(taskScheduler, parameter, clickType, cooldown * 1000L, 0);
            case "message":
                return new MessageAction(adventure, parameter, clickType, textSerializer, cooldown * 1000L, 0);
            case "server":
                return new SwitchServerAction(parameter, clickType, cooldown * 1000L, 0);
        }
        throw new IllegalArgumentException("Couldn't adapt znpcs click action: " + type);
    }
}
