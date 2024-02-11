package lol.pyr.znpcsplus;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.libraries.LibraryLoader;
import lol.pyr.znpcsplus.util.FileUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZNpcsPlusBootstrap extends JavaPlugin {
    private ZNpcsPlus zNpcsPlus;
    private boolean legacy;

    @Override
    public void onLoad() {
        legacy = new File(getDataFolder(), "data.json").isFile() && !new File(getDataFolder(), "data").isDirectory();
        if (legacy) try {
            Files.move(getDataFolder().toPath(), new File(getDataFolder().getParentFile(), "ZNPCsPlusLegacy").toPath());
        } catch (IOException e) {
            getLogger().severe(ChatColor.RED + "Failed to move legacy data folder! Plugin will disable.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Downloading and loading libraries, this might take a while if this is the first time you're launching the plugin");
        LibraryLoader loader = new LibraryLoader(this, new File(getDataFolder(), "libraries"));

        loader.addRelocation(decrypt("org..bstats"), "lol.pyr.znpcsplus.libraries.bstats");
        loader.addRelocation(decrypt("me..robertlit..spigotresources"), "lol.pyr.znpcsplus.libraries.spigotresources");
        loader.addRelocation(decrypt("net..kyori"), "lol.pyr.znpcsplus.libraries.kyori");
        loader.addRelocation(decrypt("org..checkerframework"), "lol.pyr.znpcsplus.libraries.checkerframework");
        loader.addRelocation(decrypt("com..google"), "lol.pyr.znpcsplus.libraries.google");
        loader.addRelocation(decrypt("com..github..retrooper..packetevents"), "lol.pyr.znpcsplus.libraries.packetevents.api");
        loader.addRelocation(decrypt("io..github..retrooper..packetevents"), "lol.pyr.znpcsplus.libraries.packetevents.impl");
        loader.addRelocation(decrypt("org..yaml..snakeyaml"), "lol.pyr.znpcsplus.libraries.snakeyaml");
        loader.addRelocation(decrypt("space..arim..dazzleconf"), "lol.pyr.znpcsplus.libraries.dazzleconf");
        loader.addRelocation(decrypt("lol..pyr..director"), "lol.pyr.znpcsplus.libraries.command");
        
        loader.loadLibrary(decrypt("com..google..guava"), "guava", "18.0");
        loader.loadLibrary(decrypt("com..google..code..gson"), "gson", "2.10.1");

        loader.loadLibrary(decrypt("org..bstats"), "bstats-base", "3.0.2");
        loader.loadLibrary(decrypt("org..bstats"), "bstats-bukkit", "3.0.2");

        loader.loadLibrary("me.robertlit", "SpigotResourcesAPI", "2.0", "https://repo.pyr.lol/releases");

        loader.loadLibrary(decrypt("com..github..retrooper..packetevents"), "api", "2.2.1", "https://repo.codemc.io/repository/maven-releases/");
        loader.loadLibrary(decrypt("com..github..retrooper..packetevents"), "spigot", "2.2.1", "https://repo.codemc.io/repository/maven-releases/");

        loader.loadLibrary(decrypt("space..arim..dazzleconf"), "dazzleconf-core", "1.2.1");
        loader.loadLibrary(decrypt("space..arim..dazzleconf"), "dazzleconf-ext-snakeyaml", "1.2.1");
        loader.loadLibrary("org.yaml", "snakeyaml", "1.33");

        loader.loadLibrary("lol.pyr", "director-adventure", "2.1.1", "https://repo.pyr.lol/releases");

        loader.loadLibrary(decrypt("net..kyori"), "adventure-api", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-key", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-nbt", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-platform-facet", "4.3.1");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-platform-api", "4.3.1");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-platform-bukkit", "4.3.1");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-text-minimessage", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-text-serializer-bungeecord", "4.3.1");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-text-serializer-gson", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-text-serializer-gson-legacy-impl", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-text-serializer-json", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-text-serializer-json-legacy-impl", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "adventure-text-serializer-legacy", "4.14.0");
        loader.loadLibrary(decrypt("net..kyori"), "examination-api", "1.3.0");
        loader.loadLibrary(decrypt("net..kyori"), "examination-string", "1.3.0");
        loader.deleteUnloadedLibraries();

        getLogger().info("Loaded " + loader.loadedLibraryCount() + " libraries!");
        zNpcsPlus = new ZNpcsPlus(this);
    }

    @Override
    public void onEnable() {
        if (zNpcsPlus != null) zNpcsPlus.onEnable();
    }

    @Override
    public void onDisable() {
        if (zNpcsPlus != null) zNpcsPlus.onDisable();
    }

    private final static Pattern EMBEDDED_FILE_PATTERN = Pattern.compile("\\{@(.*?)}");

    private String loadMessageFile(String file) {
        Reader reader = getTextResource("messages/" + file + ".txt");
        if (reader == null) throw new RuntimeException(file + ".txt is missing from ZNPCsPlus jar!");
        String text = FileUtil.dumpReaderAsString(reader);
        Matcher matcher = EMBEDDED_FILE_PATTERN.matcher(text);
        StringBuilder builder = new StringBuilder();
        int lastMatchEnd = 0;
        while (matcher.find()) {
            builder.append(text, lastMatchEnd, matcher.start());
            lastMatchEnd = matcher.end();
            builder.append(loadMessageFile(matcher.group(1)));
        }
        builder.append(text, lastMatchEnd, text.length());
        return builder.toString();
    }

    protected Message<CommandContext> loadHelpMessage(String name) {
        Component component = MiniMessage.miniMessage().deserialize(loadMessageFile(name));
        return context -> context.send(component);
    }

    public boolean movedLegacy() {
        return legacy;
    }

    // Ugly hack because of https://github.com/johnrengelman/shadow/issues/232
    private static String decrypt(String packageName) {
        return packageName.replace("..", ".");
    }
}
