package lol.pyr.znpcsplus;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lol.pyr.director.adventure.command.CommandManager;
import lol.pyr.director.adventure.command.MultiCommand;
import lol.pyr.znpcsplus.api.ZApiProvider;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.config.Configs;
import lol.pyr.znpcsplus.interaction.InteractionPacketListener;
import lol.pyr.znpcsplus.interaction.types.ConsoleCommandAction;
import lol.pyr.znpcsplus.interaction.types.MessageAction;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.scheduling.FoliaScheduler;
import lol.pyr.znpcsplus.scheduling.SpigotScheduler;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import lol.pyr.znpcsplus.skin.cache.SkinCacheCleanTask;
import lol.pyr.znpcsplus.skin.descriptor.FetchingDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.MirrorDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import lol.pyr.znpcsplus.tasks.NpcVisibilityTask;
import lol.pyr.znpcsplus.updater.UpdateChecker;
import lol.pyr.znpcsplus.updater.UpdateNotificationListener;
import lol.pyr.znpcsplus.user.User;
import lol.pyr.znpcsplus.user.UserListener;
import lol.pyr.znpcsplus.util.BungeeUtil;
import lol.pyr.znpcsplus.util.FoliaUtil;
import lol.pyr.znpcsplus.util.ZLocation;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.FileUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ZNpcsPlus extends JavaPlugin {
    public static Logger LOGGER;
    public static File PLUGIN_FOLDER;
    public static File PATH_FOLDER;
    private static final int PLUGIN_ID = 18244;
    public static TaskScheduler SCHEDULER;
    public static BungeeUtil BUNGEE_UTILS;
    public static BukkitAudiences ADVENTURE;
    public static boolean PLACEHOLDERS_SUPPORTED;

    private boolean enabled = false;

    public static void debug(String str) {
        if (!Configs.config().debugEnabled()) return;
        LOGGER.info("[DEBUG] " + str);
    }

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
        SCHEDULER = FoliaUtil.isFolia() ? new FoliaScheduler(this) : new SpigotScheduler(this);
        BUNGEE_UTILS = new BungeeUtil(this);
        Bukkit.getOnlinePlayers().forEach(User::get);
        registerCommands();

        log(ChatColor.WHITE + " * Starting tasks...");
        new NpcVisibilityTask();
        new SkinCacheCleanTask();
        new UserListener(this);
        if (Configs.config().checkForUpdates()) new UpdateNotificationListener(this, new UpdateChecker(this));

        ZApiProvider.register(new ZNpcsApi());
        enabled = true;
        log(ChatColor.WHITE + " * Loading complete! (" + (System.currentTimeMillis() - before) + "ms)");
        log("");

        if (Configs.config().debugEnabled()) {
            int wrap = 20;
            int x = 0;
            int z = 0;
            World world = Bukkit.getWorld("world");
            if (world == null) world = Bukkit.getWorlds().get(0);
            for (NpcType type : NpcType.values()) {
                NpcEntryImpl entry = NpcRegistryImpl.get().create("debug_npc" + (z * wrap + x), world, type, new ZLocation(x * 3, 200, z * 3, 0, 0));
                entry.setProcessed(true);
                NpcImpl npc = entry.getNpc();
                if (type.getType() == EntityTypes.PLAYER) {
                    SkinCache.fetchByName("Notch").thenAccept(skin -> npc.setProperty(EntityProperty.SKIN, new PrefetchedDescriptor(skin)));
                    npc.setProperty(EntityProperty.INVISIBLE, true);
                }
                npc.setProperty(EntityProperty.GLOW, NamedTextColor.RED);
                // npc.setProperty(EntityProperty.FIRE, true);
                npc.getHologram().addLine(Component.text("Hello, World!"));
                if (x++ > wrap) {
                    x = 0;
                    z++;
                }
            }
            NpcEntryImpl entry = NpcRegistryImpl.get().create("debug_npc" + (z * wrap + x), world, NpcType.byName("player"), new ZLocation(x * 3, 200, z * 3, 0, 0));
            entry.setProcessed(true);
            NpcImpl npc = entry.getNpc();
            npc.setProperty(EntityProperty.SKIN, new FetchingDescriptor("jeb_"));
            npc.addAction(new MessageAction(1000L, "<red>Hi, I'm jeb!"));
            x++;
            entry = NpcRegistryImpl.get().create("debug_npc" + (z * wrap + x), world, NpcType.byName("player"), new ZLocation(x * 3, 200, z * 3, 0, 0));
            entry.setProcessed(true);
            npc = entry.getNpc();
            npc.setProperty(EntityProperty.SKIN, new MirrorDescriptor());
            npc.addAction(new ConsoleCommandAction(1000L, "kick {player}"));
        }
    }

    @Override
    public void onDisable() {
        if (!enabled) return;
        ZApiProvider.unregister();
        Bukkit.getOnlinePlayers().forEach(User::remove);
        ADVENTURE.close();
        ADVENTURE = null;
    }

    private void registerCommands() {
        CommandManager manager = new CommandManager(this, ADVENTURE, context -> {});
        manager.registerCommand("npc", new MultiCommand());
    }
}
