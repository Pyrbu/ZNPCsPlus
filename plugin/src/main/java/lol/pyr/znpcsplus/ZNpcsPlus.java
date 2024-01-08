package lol.pyr.znpcsplus;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandManager;
import lol.pyr.director.adventure.command.MultiCommand;
import lol.pyr.director.adventure.parse.primitive.BooleanParser;
import lol.pyr.director.adventure.parse.primitive.DoubleParser;
import lol.pyr.director.adventure.parse.primitive.FloatParser;
import lol.pyr.director.adventure.parse.primitive.IntegerParser;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.api.NpcApiProvider;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.commands.*;
import lol.pyr.znpcsplus.commands.action.*;
import lol.pyr.znpcsplus.commands.hologram.*;
import lol.pyr.znpcsplus.commands.property.PropertyRemoveCommand;
import lol.pyr.znpcsplus.commands.property.PropertySetCommand;
import lol.pyr.znpcsplus.commands.storage.ImportCommand;
import lol.pyr.znpcsplus.commands.storage.LoadAllCommand;
import lol.pyr.znpcsplus.commands.storage.SaveAllCommand;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.conversion.DataImporterRegistry;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.interaction.InteractionPacketListener;
import lol.pyr.znpcsplus.npc.*;
import lol.pyr.znpcsplus.packets.*;
import lol.pyr.znpcsplus.parsers.*;
import lol.pyr.znpcsplus.scheduling.FoliaScheduler;
import lol.pyr.znpcsplus.scheduling.SpigotScheduler;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.skin.cache.SkinCacheCleanTask;
import lol.pyr.znpcsplus.tasks.HologramRefreshTask;
import lol.pyr.znpcsplus.tasks.NpcProcessorTask;
import lol.pyr.znpcsplus.tasks.ViewableHideOnLeaveListener;
import lol.pyr.znpcsplus.updater.UpdateChecker;
import lol.pyr.znpcsplus.updater.UpdateNotificationListener;
import lol.pyr.znpcsplus.user.UserListener;
import lol.pyr.znpcsplus.user.UserManager;
import lol.pyr.znpcsplus.util.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bstats.bukkit.Metrics;
import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ZNpcsPlus {
    private final LegacyComponentSerializer textSerializer = LegacyComponentSerializer.builder()
            .character('&')
            .hexCharacter('#')
            .hexColors().build();

    private final List<Runnable> shutdownTasks = new ArrayList<>();
    private final PacketEventsAPI<Plugin> packetEvents;
    private final ZNpcsPlusBootstrap bootstrap;

    public ZNpcsPlus(ZNpcsPlusBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        packetEvents = SpigotPacketEventsBuilder.build(bootstrap);
        PacketEvents.setAPI(packetEvents);
        packetEvents.getSettings().checkForUpdates(false);
        packetEvents.load();
    }

    private void log(String str) {
        Bukkit.getConsoleSender().sendMessage(str);
    }

    public void onEnable() {
        getDataFolder().mkdirs();

        log(ChatColor.YELLOW + "  ___       __   __  __");
        log(ChatColor.YELLOW + "   _/ |\\ | |__) |   (__` " + ChatColor.GOLD + "__|__   " + ChatColor.YELLOW + getDescription().getName() + " " + ChatColor.GOLD + "v" + getDescription().getVersion());
        log(ChatColor.YELLOW + "  /__ | \\| |    |__ .__) " + ChatColor.GOLD + "  |     " + ChatColor.GRAY + "Maintained with " + ChatColor.RED + "\u2764 " + ChatColor.GRAY + " by Pyr#6969");
        log("");

        PluginManager pluginManager = Bukkit.getPluginManager();
        long before = System.currentTimeMillis();

        log(ChatColor.WHITE + " * Initializing libraries...");

        packetEvents.init();

        BukkitAudiences adventure = BukkitAudiences.create(bootstrap);
        shutdownTasks.add(adventure::close);

        log(ChatColor.WHITE + " * Initializing components...");

        TaskScheduler scheduler = FoliaUtil.isFolia() ? new FoliaScheduler(bootstrap) : new SpigotScheduler(bootstrap);
        shutdownTasks.add(scheduler::cancelAll);

        ConfigManager configManager = new ConfigManager(getDataFolder());
        MojangSkinCache skinCache = new MojangSkinCache(configManager);
        EntityPropertyRegistryImpl propertyRegistry = new EntityPropertyRegistryImpl(skinCache, configManager);
        PacketFactory packetFactory = setupPacketFactory(scheduler, propertyRegistry, configManager);
        propertyRegistry.registerTypes(packetFactory, textSerializer);

        ActionRegistry actionRegistry = new ActionRegistry();
        NpcTypeRegistryImpl typeRegistry = new NpcTypeRegistryImpl();
        NpcRegistryImpl npcRegistry = new NpcRegistryImpl(configManager, this, packetFactory, actionRegistry,
                scheduler, typeRegistry, propertyRegistry, textSerializer);
        shutdownTasks.add(npcRegistry::unload);

        UserManager userManager = new UserManager();
        shutdownTasks.add(userManager::shutdown);
        
        BungeeConnector bungeeConnector = new BungeeConnector(bootstrap);
        DataImporterRegistry importerRegistry = new DataImporterRegistry(configManager, adventure,
                scheduler, packetFactory, textSerializer, typeRegistry, getDataFolder().getParentFile(),
                propertyRegistry, skinCache, npcRegistry, bungeeConnector);

        log(ChatColor.WHITE + " * Registerring components...");

        bungeeConnector.registerChannel();
        shutdownTasks.add(bungeeConnector::unregisterChannel);

        typeRegistry.registerDefault(packetEvents, propertyRegistry);
        actionRegistry.registerTypes(scheduler, adventure, textSerializer, bungeeConnector);
        packetEvents.getEventManager().registerListener(new InteractionPacketListener(userManager, npcRegistry, scheduler), PacketListenerPriority.MONITOR);
        new Metrics(bootstrap, 18244);
        pluginManager.registerEvents(new UserListener(userManager), bootstrap);

        registerCommands(npcRegistry, skinCache, adventure, actionRegistry,
                typeRegistry, propertyRegistry, importerRegistry, configManager);

        log(ChatColor.WHITE + " * Starting tasks...");
        if (configManager.getConfig().checkForUpdates()) {
            UpdateChecker updateChecker = new UpdateChecker(getDescription());
            scheduler.runLaterAsync(() -> {
                scheduler.runDelayedTimerAsync(updateChecker, 0L, 6000L);
                shutdownTasks.add(updateChecker::shutdown);
            }, 5L);
            pluginManager.registerEvents(new UpdateNotificationListener(this, adventure, updateChecker, scheduler), bootstrap);
        }

        scheduler.runDelayedTimerAsync(new NpcProcessorTask(npcRegistry, propertyRegistry), 60L, 3L);
        scheduler.runDelayedTimerAsync(new HologramRefreshTask(npcRegistry), 60L, 20L);
        scheduler.runDelayedTimerAsync(new SkinCacheCleanTask(skinCache), 1200, 1200);
        pluginManager.registerEvents(new ViewableHideOnLeaveListener(), bootstrap);

        log(ChatColor.WHITE + " * Loading data...");
        npcRegistry.reload();
        if (configManager.getConfig().autoSaveEnabled()) shutdownTasks.add(npcRegistry::save);

        if (bootstrap.movedLegacy()) {
            log(ChatColor.WHITE + " * Converting legacy data...");
            try {
                Collection<NpcEntryImpl> entries = importerRegistry.getImporter("znpcsplus_legacy").importData();
                npcRegistry.registerAll(entries);
            } catch (Exception exception) {
                log(ChatColor.RED + " * Legacy data conversion failed! Check conversion.log for more info.");
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(new File(getDataFolder(), "conversion.log").toPath(), StandardOpenOption.CREATE_NEW))) {
                    exception.printStackTrace(writer);
                } catch (IOException e) {
                    log(ChatColor.DARK_RED + " * Critical error! Writing to conversion.log failed.");
                    e.printStackTrace();
                }
            }
        }

        NpcApiProvider.register(bootstrap, new ZNpcsPlusApi(npcRegistry, typeRegistry, propertyRegistry, skinCache));
        log(ChatColor.WHITE + " * Loading complete! (" + (System.currentTimeMillis() - before) + "ms)");
        log("");

        if (configManager.getConfig().debugEnabled()) {
            World world = Bukkit.getWorld("world");
            if (world == null) world = Bukkit.getWorlds().get(0);
            int i = 0;
            for (NpcTypeImpl type : typeRegistry.getAllImpl()) {
                NpcEntryImpl entry = npcRegistry.create("debug_npc_" + i, world, type, new NpcLocation(i * 3, 200, 0, 0, 0));
                entry.setProcessed(true);
                NpcImpl npc = entry.getNpc();
                npc.getHologram().addTextLineComponent(Component.text("Hello, World!", TextColor.color(255, 0, 0)));
                npc.getHologram().addTextLineComponent(Component.text("Hello, World!", TextColor.color(0, 255, 0)));
                npc.getHologram().addTextLineComponent(Component.text("Hello, World!", TextColor.color(0, 0, 255)));
                i++;
            }
        }
    }

    public void onDisable() {
        NpcApiProvider.unregister();
        for (Runnable runnable : shutdownTasks) try {
            runnable.run();
        } catch (Throwable throwable) {
            bootstrap.getLogger().severe("One of the registered shutdown tasks threw an exception:");
            throwable.printStackTrace();
        }
        shutdownTasks.clear();
        PacketEvents.getAPI().terminate();
    }

    private PacketFactory setupPacketFactory(TaskScheduler scheduler, EntityPropertyRegistryImpl propertyRegistry, ConfigManager configManager) {
        HashMap<ServerVersion, LazyLoader<? extends PacketFactory>> versions = new HashMap<>();
        versions.put(ServerVersion.V_1_8, LazyLoader.of(() -> new V1_8PacketFactory(scheduler, packetEvents, propertyRegistry, textSerializer, configManager)));
        versions.put(ServerVersion.V_1_17, LazyLoader.of(() -> new V1_17PacketFactory(scheduler, packetEvents, propertyRegistry, textSerializer, configManager)));
        versions.put(ServerVersion.V_1_19_3, LazyLoader.of(() -> new V1_19_3PacketFactory(scheduler, packetEvents, propertyRegistry, textSerializer, configManager)));
        versions.put(ServerVersion.V_1_20_2, LazyLoader.of(() -> new V1_20_2PacketFactory(scheduler, packetEvents, propertyRegistry, textSerializer, configManager)));

        ServerVersion version = packetEvents.getServerManager().getVersion();
        if (versions.containsKey(version)) return versions.get(version).get();
        for (ServerVersion v : ServerVersion.reversedValues()) {
            if (v.isNewerThan(version)) continue;
            if (!versions.containsKey(v)) continue;
            return versions.get(v).get();
        }
        throw new RuntimeException("Unsupported version!");
    }

    private void registerCommands(NpcRegistryImpl npcRegistry, MojangSkinCache skinCache, BukkitAudiences adventure,
                                  ActionRegistry actionRegistry, NpcTypeRegistryImpl typeRegistry,
                                  EntityPropertyRegistryImpl propertyRegistry, DataImporterRegistry importerRegistry,
                                  ConfigManager configManager) {

        Message<CommandContext> incorrectUsageMessage = context -> context.send(Component.text("Incorrect usage: /" + context.getUsage(), NamedTextColor.RED));
        CommandManager manager = new CommandManager(bootstrap, adventure, incorrectUsageMessage);

        manager.registerParser(NpcTypeImpl.class, new NpcTypeParser(incorrectUsageMessage, typeRegistry));
        manager.registerParser(NpcEntryImpl.class, new NpcEntryParser(npcRegistry, incorrectUsageMessage));
        manager.registerParser(EntityPropertyImpl.class, new EntityPropertyParser(incorrectUsageMessage, propertyRegistry));
        manager.registerParser(Integer.class, new IntegerParser(incorrectUsageMessage));
        manager.registerParser(Double.class, new DoubleParser(incorrectUsageMessage));
        manager.registerParser(Float.class, new FloatParser(incorrectUsageMessage));
        manager.registerParser(Boolean.class, new BooleanParser(incorrectUsageMessage));
        manager.registerParser(NamedColor.class, new NamedColorParser(incorrectUsageMessage));
        manager.registerParser(InteractionType.class, new InteractionTypeParser(incorrectUsageMessage));
        manager.registerParser(Color.class, new ColorParser(incorrectUsageMessage));
        manager.registerParser(Vector3f.class, new Vector3fParser(incorrectUsageMessage));
        manager.registerParser(String.class, new StringParser(incorrectUsageMessage));
        manager.registerParser(Vector3i.class, new Vector3iParser(incorrectUsageMessage));

        // TODO: Need to find a better way to do this
        registerEnumParser(manager, NpcPose.class, incorrectUsageMessage);
        registerEnumParser(manager, DyeColor.class, incorrectUsageMessage);
        registerEnumParser(manager, CatVariant.class, incorrectUsageMessage);
        registerEnumParser(manager, CreeperState.class, incorrectUsageMessage);
        registerEnumParser(manager, ParrotVariant.class, incorrectUsageMessage);
        registerEnumParser(manager, SpellType.class, incorrectUsageMessage);
        registerEnumParser(manager, FoxVariant.class, incorrectUsageMessage);
        registerEnumParser(manager, FrogVariant.class, incorrectUsageMessage);
        registerEnumParser(manager, VillagerType.class, incorrectUsageMessage);
        registerEnumParser(manager, VillagerProfession.class, incorrectUsageMessage);
        registerEnumParser(manager, VillagerLevel.class, incorrectUsageMessage);
        registerEnumParser(manager, AxolotlVariant.class, incorrectUsageMessage);
        registerEnumParser(manager, HorseType.class, incorrectUsageMessage);
        registerEnumParser(manager, HorseStyle.class, incorrectUsageMessage);
        registerEnumParser(manager, HorseColor.class, incorrectUsageMessage);
        registerEnumParser(manager, HorseArmor.class, incorrectUsageMessage);
        registerEnumParser(manager, LlamaVariant.class, incorrectUsageMessage);
        registerEnumParser(manager, MooshroomVariant.class, incorrectUsageMessage);
        registerEnumParser(manager, OcelotType.class, incorrectUsageMessage);
        registerEnumParser(manager, PandaGene.class, incorrectUsageMessage);
        registerEnumParser(manager, PuffState.class, incorrectUsageMessage);
        registerEnumParser(manager, LookType.class, incorrectUsageMessage);
        registerEnumParser(manager, TropicalFishVariant.TropicalFishPattern.class, incorrectUsageMessage);
        registerEnumParser(manager, SnifferState.class, incorrectUsageMessage);
        registerEnumParser(manager, RabbitType.class, incorrectUsageMessage);
        registerEnumParser(manager, AttachDirection.class, incorrectUsageMessage);

        manager.registerCommand("npc", new MultiCommand(bootstrap.loadHelpMessage("root"))
                .addSubcommand("center", new CenterCommand(npcRegistry))
                .addSubcommand("create", new CreateCommand(npcRegistry, typeRegistry))
                .addSubcommand("reloadconfig", new ReloadConfigCommand(configManager))
                .addSubcommand("toggle", new ToggleCommand(npcRegistry))
                .addSubcommand("skin", new SkinCommand(skinCache, npcRegistry, typeRegistry, propertyRegistry))
                .addSubcommand("delete", new DeleteCommand(npcRegistry, adventure))
                .addSubcommand("move", new MoveCommand(npcRegistry))
                .addSubcommand("teleport", new TeleportCommand(npcRegistry))
                .addSubcommand("list", new ListCommand(npcRegistry))
                .addSubcommand("near", new NearCommand(npcRegistry))
                .addSubcommand("type", new TypeCommand(npcRegistry, typeRegistry))
                .addSubcommand("setlocation", new SetLocationCommand(npcRegistry))
                .addSubcommand("lookatme", new LookAtMeCommand(npcRegistry))
                .addSubcommand("setrotation", new SetRotationCommand(npcRegistry))
                .addSubcommand("changeid", new ChangeIdCommand(npcRegistry))
                .addSubcommand("property", new MultiCommand(bootstrap.loadHelpMessage("property"))
                        .addSubcommand("set", new PropertySetCommand(npcRegistry))
                        .addSubcommand("remove", new PropertyRemoveCommand(npcRegistry)))
                .addSubcommand("storage", new MultiCommand(bootstrap.loadHelpMessage("storage"))
                        .addSubcommand("save", new SaveAllCommand(npcRegistry))
                        .addSubcommand("reload", new LoadAllCommand(npcRegistry))
                        .addSubcommand("import", new ImportCommand(npcRegistry, importerRegistry)))
                .addSubcommand("holo", new MultiCommand(bootstrap.loadHelpMessage("holo"))
                        .addSubcommand("add", new HoloAddCommand(npcRegistry))
                        .addSubcommand("additem", new HoloAddItemCommand(npcRegistry))
                        .addSubcommand("delete", new HoloDeleteCommand(npcRegistry))
                        .addSubcommand("info", new HoloInfoCommand(npcRegistry))
                        .addSubcommand("insert", new HoloInsertCommand(npcRegistry))
                        .addSubcommand("insertitem", new HoloInsertItemCommand(npcRegistry))
                        .addSubcommand("set", new HoloSetCommand(npcRegistry))
                        .addSubcommand("setitem", new HoloSetItemCommand(npcRegistry))
                        .addSubcommand("offset", new HoloOffsetCommand(npcRegistry))
                        .addSubcommand("refreshdelay", new HoloRefreshDelayCommand(npcRegistry)))
                .addSubcommand("action", new MultiCommand(bootstrap.loadHelpMessage("action"))
                        .addSubcommand("add", new ActionAddCommand(npcRegistry, actionRegistry))
                        .addSubcommand("clear", new ActionClearCommand(npcRegistry))
                        .addSubcommand("delete", new ActionDeleteCommand(npcRegistry))
                        .addSubcommand("edit", new ActionEditCommand(npcRegistry, actionRegistry))
                        .addSubcommand("list", new ActionListCommand(npcRegistry)))
        );
    }

    private <T extends Enum<T>> void registerEnumParser(CommandManager manager, Class<T> clazz, Message<CommandContext> message) {
        manager.registerParser(clazz, new EnumParser<>(clazz, message));
    }

    public File getDataFolder() {
        return bootstrap.getDataFolder();
    }

    public PluginDescriptionFile getDescription() {
        return bootstrap.getDescription();
    }
}
