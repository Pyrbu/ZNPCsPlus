package lol.pyr.znpcsplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.znetworkw.znpcservers.commands.list.DefaultCommand;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.listeners.InventoryListener;
import io.github.znetworkw.znpcservers.listeners.PlayerListener;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCModel;
import io.github.znetworkw.znpcservers.npc.NPCPath;
import io.github.znetworkw.znpcservers.npc.NPCType;
import io.github.znetworkw.znpcservers.npc.task.NPCPathTask;
import io.github.znetworkw.znpcservers.npc.task.NPCSaveTask;
import io.github.znetworkw.znpcservers.npc.task.NPCUpdateTask;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.BungeeUtils;
import io.github.znetworkw.znpcservers.utility.SchedulerUtils;
import io.github.znetworkw.znpcservers.utility.itemstack.ItemStackSerializer;
import io.github.znetworkw.znpcservers.utility.location.ZLocation;
import org.apache.commons.io.FileUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

public class ZNPCsPlus extends JavaPlugin {
    public static Logger LOGGER;
    public static File PLUGIN_FOLDER;
    public static File PATH_FOLDER;
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZLocation.class, ZLocation.SERIALIZER)
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackSerializer())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private static final int PLUGIN_ID = 18244;
    public static SchedulerUtils SCHEDULER;
    public static BungeeUtils BUNGEE_UTILS;

    private boolean enabled = false;

    public static NPC createNPC(int id, NPCType npcType, Location location, String name) {
        NPC find = NPC.find(id);
        if (find != null)
            return find;
        NPCModel pojo = (new NPCModel(id)).withHologramLines(Collections.singletonList(name)).withLocation(new ZLocation(location)).withNpcType(npcType);
        ConfigurationConstants.NPC_LIST.add(pojo);
        return new NPC(pojo, true);
    }

    public static void deleteNPC(int npcID) {
        NPC npc = NPC.find(npcID);
        if (npc == null) throw new IllegalStateException("can't find npc:  " + npcID);
        NPC.unregister(npcID);
        ConfigurationConstants.NPC_LIST.remove(npc.getNpcPojo());
    }

    @Override
    public void onLoad() {
        LOGGER = getLogger();
        PLUGIN_FOLDER = getDataFolder();
        PATH_FOLDER = new File(PLUGIN_FOLDER, "paths");
    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("ServersNPC")) {
            LOGGER.severe("Detected old version of ZNPCs! Disabling the plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        checkOldFolder();
        PLUGIN_FOLDER.mkdirs();
        PATH_FOLDER.mkdirs();

        loadAllPaths();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new Metrics(this, PLUGIN_ID);
        new DefaultCommand();
        SCHEDULER = new SchedulerUtils(this);
        BUNGEE_UTILS = new BungeeUtils(this);
        Bukkit.getOnlinePlayers().forEach(ZUser::find);
        new NPCPathTask(this);
        new NPCUpdateTask(this);
        new NPCSaveTask(this, ConfigurationConstants.SAVE_DELAY);
        new PlayerListener(this);
        new InventoryListener(this);
        enabled = true;
    }

    @Override
    public void onDisable() {
        if (!enabled) return;
        Configuration.SAVE_CONFIGURATIONS.forEach(Configuration::save);
        Bukkit.getOnlinePlayers().forEach(ZUser::unregister);
    }

    public void loadAllPaths() {
        File[] files = PATH_FOLDER.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (!file.getName().endsWith(".path")) continue;
            NPCPath.AbstractTypeWriter abstractTypeWriter = NPCPath.AbstractTypeWriter.forFile(file, NPCPath.AbstractTypeWriter.TypeWriter.MOVEMENT);
            abstractTypeWriter.load();
        }
    }

    private void checkOldFolder() {
        if (PLUGIN_FOLDER.exists()) return;
        File oldFolder = new File(PLUGIN_FOLDER.getParent(), "ServersNPC");
        if (!oldFolder.exists()) return;
        LOGGER.info("Detected old ZNPCs files and no new ones present, converting...");
        try {
            FileUtils.moveDirectory(oldFolder, PLUGIN_FOLDER);
        } catch (IOException e) {
            LOGGER.severe("Failed to convert old ZNPCs files:");
            e.printStackTrace();
        }
    }
}
