package lol.pyr.znpcsplus;

import com.google.common.collect.ImmutableList;
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
import io.github.znetworkw.znpcservers.npc.task.NPCManagerTask;
import io.github.znetworkw.znpcservers.npc.task.NPCSaveTask;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.BungeeUtils;
import io.github.znetworkw.znpcservers.utility.SchedulerUtils;
import io.github.znetworkw.znpcservers.utility.itemstack.ItemStackSerializer;
import io.github.znetworkw.znpcservers.utility.location.ZLocation;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;

public class ZNPCsPlus extends JavaPlugin {
    public static final File PLUGIN_FOLDER = new File("plugins/ServersNPC");

    public static final File PATH_FOLDER = new File("plugins/ServersNPC/paths");
    public static final Gson GSON = (new GsonBuilder())
            .registerTypeAdapter(ZLocation.class, ZLocation.SERIALIZER)
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackSerializer())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private static final int PLUGIN_ID = 8054;
    public static SchedulerUtils SCHEDULER;
    public static BungeeUtils BUNGEE_UTILS;

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
        if (npc == null)
            throw new IllegalStateException("can't find npc:  " + npcID);
        NPC.unregister(npcID);
        ConfigurationConstants.NPC_LIST.remove(npc.getNpcPojo());
    }

    public void onEnable() {
        ImmutableList<File> files = ImmutableList.of(PLUGIN_FOLDER, PATH_FOLDER);
        for (File file : files) file.mkdirs();

        loadAllPaths();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new Metrics(this, PLUGIN_ID);
        new DefaultCommand();
        SCHEDULER = new SchedulerUtils(this);
        BUNGEE_UTILS = new BungeeUtils(this);
        Bukkit.getOnlinePlayers().forEach(ZUser::find);
        new NPCManagerTask(this);
        new NPCSaveTask(this, ConfigurationConstants.SAVE_DELAY);
        new PlayerListener(this);
        new InventoryListener(this);
    }

    public void onDisable() {
        Configuration.SAVE_CONFIGURATIONS.forEach(Configuration::save);
        Bukkit.getOnlinePlayers().forEach(ZUser::unregister);
    }

    public void loadAllPaths() {
        File[] listFiles = PATH_FOLDER.listFiles();
        if (listFiles == null)
            return;
        for (File file : listFiles) {
            if (file.getName().endsWith(".path")) {
                NPCPath.AbstractTypeWriter abstractTypeWriter = NPCPath.AbstractTypeWriter.forFile(file, NPCPath.AbstractTypeWriter.TypeWriter.MOVEMENT);
                abstractTypeWriter.load();
            }
        }
    }
}
