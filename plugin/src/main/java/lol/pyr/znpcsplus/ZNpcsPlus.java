package lol.pyr.znpcsplus;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lol.pyr.director.adventure.command.CommandManager;
import lol.pyr.director.adventure.command.MultiCommand;
import lol.pyr.director.adventure.parse.primitive.BooleanParser;
import lol.pyr.director.adventure.parse.primitive.IntegerParser;
import lol.pyr.znpcsplus.api.ZApi;
import lol.pyr.znpcsplus.api.ZApiProvider;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.commands.*;
import lol.pyr.znpcsplus.commands.hologram.*;
import lol.pyr.znpcsplus.commands.parsers.EntityPropertyParser;
import lol.pyr.znpcsplus.commands.parsers.NamedTextColorParser;
import lol.pyr.znpcsplus.commands.parsers.NpcEntryParser;
import lol.pyr.znpcsplus.commands.parsers.NpcTypeParser;
import lol.pyr.znpcsplus.commands.storage.LoadAllCommand;
import lol.pyr.znpcsplus.commands.storage.SaveAllCommand;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.interaction.InteractionPacketListener;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.metadata.*;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import lol.pyr.znpcsplus.packets.*;
import lol.pyr.znpcsplus.scheduling.FoliaScheduler;
import lol.pyr.znpcsplus.scheduling.SpigotScheduler;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import lol.pyr.znpcsplus.skin.cache.SkinCacheCleanTask;
import lol.pyr.znpcsplus.tasks.NpcVisibilityTask;
import lol.pyr.znpcsplus.updater.UpdateChecker;
import lol.pyr.znpcsplus.updater.UpdateNotificationListener;
import lol.pyr.znpcsplus.user.UserListener;
import lol.pyr.znpcsplus.user.UserManager;
import lol.pyr.znpcsplus.util.BungeeUtil;
import lol.pyr.znpcsplus.util.FoliaUtil;
import lol.pyr.znpcsplus.util.LazyLoader;
import lol.pyr.znpcsplus.util.ZLocation;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ZNpcsPlus extends JavaPlugin implements ZApi {
    private static final int PLUGIN_ID = 18244;

    private TaskScheduler scheduler;
    private BukkitAudiences adventure;
    private SkinCache skinCache;

    private MetadataFactory metadataFactory;

    private NpcRegistryImpl npcRegistry;

    private UserManager userManager;
    private final LegacyComponentSerializer textSerializer = LegacyComponentSerializer.builder()
            .character('&')
            .hexCharacter('#')
            .hexColors().build();
    private PacketEventsAPI<Plugin> packetEvents;

    private boolean enabled = false;

    @Override
    public void onLoad() {
        packetEvents = SpigotPacketEventsBuilder.build(this);
        PacketEvents.setAPI(packetEvents);
        packetEvents.getSettings().checkForUpdates(false);
        packetEvents.load();
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

        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.isPluginEnabled("ServersNPC")) {
            log(ChatColor.DARK_RED + " * Detected old version of ZNPCs! Disabling the plugin.");
            log("");
            pluginManager.disablePlugin(this);
            return;
        }
        long before = System.currentTimeMillis();

        log(ChatColor.WHITE + " * Initializing Adventure...");
        adventure = BukkitAudiences.create(this);

        metadataFactory = setupMetadataFactory();
        PacketFactory packetFactory = setupPacketFactory();

        getDataFolder().mkdirs();

        log(ChatColor.WHITE + " * Loading configurations...");
        ConfigManager configManager = new ConfigManager(getDataFolder());

        log(ChatColor.WHITE + " * Defining NPC types...");
        NpcTypeImpl.defineTypes();

        log(ChatColor.WHITE + " * Registering components...");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new Metrics(this, PLUGIN_ID);
        scheduler = FoliaUtil.isFolia() ? new FoliaScheduler(this) : new SpigotScheduler(this);
        BungeeUtil bungeeUtil = new BungeeUtil(this);
        userManager = new UserManager();
        Bukkit.getOnlinePlayers().forEach(userManager::get);
        pluginManager.registerEvents(new UserListener(userManager), this);


        if (configManager.getConfig().checkForUpdates()) {
            UpdateChecker updateChecker = new UpdateChecker(this.getDescription());
            scheduler.runDelayedTimerAsync(updateChecker, 5L, 6000L);
            pluginManager.registerEvents(new UpdateNotificationListener(this, adventure, updateChecker), this);
        }

        log(ChatColor.WHITE+  " * Loading NPCs...");
        ActionRegistry actionRegistry = new ActionRegistry(scheduler, adventure, bungeeUtil);
        npcRegistry = new NpcRegistryImpl(configManager, this, packetFactory, actionRegistry);
        npcRegistry.reload();

        log(ChatColor.WHITE + " * Initializing PacketEvents...");
        packetEvents.getEventManager().registerListener(new InteractionPacketListener(userManager, npcRegistry), PacketListenerPriority.MONITOR);
        packetEvents.init();

        log(ChatColor.WHITE + " * Starting tasks...");
        scheduler.runDelayedTimerAsync(new NpcVisibilityTask(npcRegistry, configManager), 60L, 10L);
        skinCache = new SkinCache(configManager);
        scheduler.runDelayedTimerAsync(new SkinCacheCleanTask(skinCache), 1200, 1200);

        log(ChatColor.WHITE + " * Registering commands...");
        registerCommands();

        ZApiProvider.register(this);
        enabled = true;
        log(ChatColor.WHITE + " * Loading complete! (" + (System.currentTimeMillis() - before) + "ms)");
        log("");

        if (configManager.getConfig().debugEnabled()) {
            World world = Bukkit.getWorld("world");
            if (world == null) world = Bukkit.getWorlds().get(0);
            int i = 0;
            for (NpcTypeImpl type : NpcTypeImpl.values()) {
                NpcEntryImpl entry = npcRegistry.create("debug_npc_" + i, world, type, new ZLocation(i * 3, 200, 0, 0, 0));
                entry.setProcessed(true);
                NpcImpl npc = entry.getNpc();
                npc.getHologram().addLine(Component.text("Hello, World!"));
                i++;
            }
        }
    }

    private PacketFactory setupPacketFactory() {
        HashMap<ServerVersion, LazyLoader<? extends PacketFactory>> versions = new HashMap<>();
        versions.put(ServerVersion.V_1_8, LazyLoader.of(() -> new V1_8PacketFactory(scheduler, metadataFactory)));
        versions.put(ServerVersion.V_1_9, LazyLoader.of(() -> new V1_9PacketFactory(scheduler, metadataFactory)));
        versions.put(ServerVersion.V_1_10, LazyLoader.of(() -> new V1_10PacketFactory(scheduler, metadataFactory)));
        versions.put(ServerVersion.V_1_14, LazyLoader.of(() -> new V1_14PacketFactory(scheduler, metadataFactory)));
        versions.put(ServerVersion.V_1_19, LazyLoader.of(() -> new V1_19PacketFactory(scheduler, metadataFactory)));

        ServerVersion version = packetEvents.getServerManager().getVersion();
        if (versions.containsKey(version)) return versions.get(version).get();
        for (ServerVersion v : ServerVersion.reversedValues()) {
            if (v.isNewerThan(version)) continue;
            if (!versions.containsKey(v)) continue;
            return versions.get(v).get();
        }
        throw new RuntimeException("Unsupported version!");
    }

    private MetadataFactory setupMetadataFactory() {
        HashMap<ServerVersion, LazyLoader<? extends MetadataFactory>> versions = new HashMap<>();
        versions.put(ServerVersion.V_1_8, LazyLoader.of(V1_8MetadataFactory::new));
        versions.put(ServerVersion.V_1_9, LazyLoader.of(V1_9MetadataFactory::new));
        versions.put(ServerVersion.V_1_10, LazyLoader.of(V1_10MetadataFactory::new));
        versions.put(ServerVersion.V_1_13, LazyLoader.of(V1_13MetadataFactory::new));
        versions.put(ServerVersion.V_1_14, LazyLoader.of(V1_14MetadataFactory::new));
        versions.put(ServerVersion.V_1_16, LazyLoader.of(V1_16MetadataFactory::new));
        versions.put(ServerVersion.V_1_17, LazyLoader.of(V1_17MetadataFactory::new));

        ServerVersion version = packetEvents.getServerManager().getVersion();
        if (versions.containsKey(version)) return versions.get(version).get();
        for (ServerVersion v : ServerVersion.reversedValues()) {
            if (v.isNewerThan(version)) continue;
            if (!versions.containsKey(v)) continue;
            return versions.get(v).get();
        }
        throw new RuntimeException("Unsupported version!");
    }


    @Override
    public void onDisable() {
        if (!enabled) return;
        scheduler.cancelAll();
        npcRegistry.save();
        ZApiProvider.unregister();
        Bukkit.getOnlinePlayers().forEach(userManager::remove);
        adventure.close();
        adventure = null;
    }

    private void registerCommands() {
        // TODO: Messages in here
        CommandManager manager = new CommandManager(this, adventure, context -> {});

        manager.registerParser(NpcTypeImpl.class, new NpcTypeParser(context -> {}));
        manager.registerParser(NpcEntryImpl.class, new NpcEntryParser(npcRegistry, context -> {}));
        manager.registerParser(EntityPropertyImpl.class, new EntityPropertyParser(context -> {}));
        manager.registerParser(Integer.class, new IntegerParser(context -> {}));
        manager.registerParser(Boolean.class, new BooleanParser(context -> {}));
        manager.registerParser(NamedTextColor.class, new NamedTextColorParser(context -> {}));

        manager.registerCommand("npc", new MultiCommand()
                .addSubcommand("action", new ActionCommand())
                .addSubcommand("create", new CreateCommand(npcRegistry))
                .addSubcommand("skin", new SkinCommand(skinCache, npcRegistry))
                .addSubcommand("delete", new DeleteCommand(npcRegistry, adventure))
                .addSubcommand("move", new MoveCommand(npcRegistry))
                .addSubcommand("properties", new PropertiesCommand(npcRegistry))
                .addSubcommand("teleport", new TeleportCommand(npcRegistry))
                .addSubcommand("list", new ListCommand(npcRegistry))
                .addSubcommand("near", new NearCommand(npcRegistry))
                .addSubcommand("type", new TypeCommand(npcRegistry))
                .addSubcommand("storage", new MultiCommand()
                        .addSubcommand("save", new SaveAllCommand(npcRegistry))
                        .addSubcommand("reload", new LoadAllCommand(npcRegistry)))
                .addSubcommand("holo", new MultiCommand()
                        .addSubcommand("add", new HoloAddCommand(npcRegistry, textSerializer))
                        .addSubcommand("delete", new HoloDeleteCommand(npcRegistry))
                        .addSubcommand("info", new HoloInfoCommand(npcRegistry))
                        .addSubcommand("insert", new HoloInsertCommand(npcRegistry, textSerializer))
                        .addSubcommand("set", new HoloSetCommand(npcRegistry, textSerializer))
                )
        );
    }

    @Override
    public NpcRegistry getNpcRegistry() {
        return npcRegistry;
    }
}
