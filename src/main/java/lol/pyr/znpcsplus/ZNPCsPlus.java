package lol.pyr.znpcsplus;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.github.znetworkw.znpcservers.listeners.InventoryListener;
import io.github.znetworkw.znpcservers.utility.BungeeUtils;
import io.github.znetworkw.znpcservers.utility.SchedulerUtils;
import io.github.znetworkw.znpcservers.utility.itemstack.ItemStackSerializer;
import lol.pyr.znpcsplus.config.Configs;
import lol.pyr.znpcsplus.entity.EntityProperty;
import lol.pyr.znpcsplus.entity.PacketLocation;
import lol.pyr.znpcsplus.interaction.InteractionPacketListener;
import lol.pyr.znpcsplus.npc.NPC;
import lol.pyr.znpcsplus.npc.NPCRegistry;
import lol.pyr.znpcsplus.npc.NPCType;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import lol.pyr.znpcsplus.skin.cache.SkinCacheCleanTask;
import lol.pyr.znpcsplus.skin.descriptor.FetchingDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.MirrorDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import lol.pyr.znpcsplus.tasks.NPCVisibilityTask;
import lol.pyr.znpcsplus.updater.UpdateChecker;
import lol.pyr.znpcsplus.updater.UpdateNotificationListener;
import lol.pyr.znpcsplus.user.User;
import lol.pyr.znpcsplus.user.UserListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.FileUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ZNPCsPlus extends JavaPlugin {
    public static Logger LOGGER;
    public static File PLUGIN_FOLDER;
    public static File PATH_FOLDER;
    public static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackSerializer())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private static final int PLUGIN_ID = 18244;
    public static SchedulerUtils SCHEDULER;
    public static BungeeUtils BUNGEE_UTILS;
    public static BukkitAudiences ADVENTURE;
    public static boolean PLACEHOLDERS_SUPPORTED;

    private boolean enabled = false;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().checkForUpdates(false);
        PacketEvents.getAPI().load();
        LOGGER = getLogger();
        PLUGIN_FOLDER = getDataFolder();
        PATH_FOLDER = new File(PLUGIN_FOLDER, "paths");
    }

    private void log(String str) {
        Bukkit.getConsoleSender().sendMessage(str);
    }

    @Override
    public void onEnable() {
        log(ChatColor.YELLOW + "  ___       __   __  __");
        log(ChatColor.YELLOW + "   _/ |\\ | |__) |   (__` " + ChatColor.GOLD + "__|__   " + ChatColor.YELLOW + getDescription().getName() + " " + ChatColor.GOLD + "v" + getDescription().getVersion());
        log(ChatColor.YELLOW + "  /__ | \\| |    |__ .__) " + ChatColor.GOLD + "  |     " + ChatColor.GRAY + "Maintained with " + ChatColor.RED + "\u2764 " + ChatColor.GRAY + " by Pyr#6969");
        log("");

        if (Bukkit.getPluginManager().isPluginEnabled("ServersNPC")) {
            log(ChatColor.DARK_RED + " * Detected old version of ZNPCs! Disabling the plugin.");
            log("");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        long before = System.currentTimeMillis();

        File oldFolder = new File(PLUGIN_FOLDER.getParent(), "ServersNPC");
        if (!PLUGIN_FOLDER.exists() && oldFolder.exists()) {
            log(ChatColor.WHITE + " * Converting old ZNPCs files...");
            try {
                FileUtils.moveDirectory(oldFolder, PLUGIN_FOLDER);
            } catch (IOException e) {
                log(ChatColor.RED + " * Failed to convert old ZNPCs files" + (e.getMessage() == null ? "" : " due to " + e.getMessage()));
            }
        }

        log(ChatColor.WHITE + " * Initializing Adventure...");
        ADVENTURE = BukkitAudiences.create(this);

        log(ChatColor.WHITE + " * Initializing PacketEvents...");
        PacketEvents.getAPI().getEventManager().registerListener(new InteractionPacketListener(), PacketListenerPriority.MONITOR);
        PacketEvents.getAPI().init();

        PLACEHOLDERS_SUPPORTED = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        if (PLACEHOLDERS_SUPPORTED) log(ChatColor.WHITE + " * Enabling PlaceholderAPI Support...");

        PLUGIN_FOLDER.mkdirs();
        PATH_FOLDER.mkdirs();

        log(ChatColor.WHITE + " * Loading configurations...");
        Configs.init(PLUGIN_FOLDER);

        log(ChatColor.WHITE + " * Registering components...");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new Metrics(this, PLUGIN_ID);
        SCHEDULER = new SchedulerUtils(this);
        BUNGEE_UTILS = new BungeeUtils(this);
        Bukkit.getOnlinePlayers().forEach(User::get);

        log(ChatColor.WHITE + " * Starting tasks...");
        new NPCVisibilityTask(this);
        new InventoryListener(this);
        new SkinCacheCleanTask(this);
        new UserListener(this);
        if (Configs.config().checkForUpdates()) new UpdateNotificationListener(this, new UpdateChecker(this));

        enabled = true;
        log(ChatColor.WHITE + " * Loading complete! (" + (System.currentTimeMillis() - before) + "ms)");
        log("");

        if (Configs.config().debugEnabled()) {
            int wrap = 20;
            int x = 0;
            int z = 0;
            World world = Bukkit.getWorld("world");
            if (world == null) world = Bukkit.getWorlds().get(0);
            for (NPCType type : NPCType.values()) {
                NPC npc = new NPC(world, type, new PacketLocation(x * 3, 200, z * 3, 0, 0));
                if (type.getType() == EntityTypes.PLAYER) {
                    SkinCache.fetchByName("Notch").thenAccept(skin -> npc.setProperty(EntityProperty.SKIN, new PrefetchedDescriptor(skin)));
                    npc.setProperty(EntityProperty.INVISIBLE, true);
                }
                npc.setProperty(EntityProperty.GLOW, NamedTextColor.RED);
                npc.setProperty(EntityProperty.FIRE, true);
                NPCRegistry.register("debug_npc" + (z * wrap + x), npc);
                if (x++ > wrap) {
                    x = 0;
                    z++;
                }
            }
            NPC npc = new NPC(world, NPCType.byName("player"), new PacketLocation(x * 3, 200, z * 3, 0, 0));
            npc.setProperty(EntityProperty.SKIN, new FetchingDescriptor("jeb_"));
            NPCRegistry.register("debug_npc" + (z * wrap + x), npc);
            x++;
            npc = new NPC(world, NPCType.byName("player"), new PacketLocation(x * 3, 200, z * 3, 0, 0));
            npc.setProperty(EntityProperty.SKIN, new MirrorDescriptor());
            NPCRegistry.register("debug_npc" + (z * wrap + x), npc);
        }
    }

    @Override
    public void onDisable() {
        if (!enabled) return;
        Bukkit.getOnlinePlayers().forEach(User::remove);
        ADVENTURE.close();
        ADVENTURE = null;
    }
}
