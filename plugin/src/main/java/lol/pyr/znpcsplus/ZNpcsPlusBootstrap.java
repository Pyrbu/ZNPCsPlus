package lol.pyr.znpcsplus;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.util.FileUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ZNpcsPlusBootstrap extends JavaPlugin {
  private ZNpcsPlus zNpcsPlus;
  private boolean legacy;

  @Override
  public void onLoad() {
    legacy =
        new File(getDataFolder(), "data.json").isFile()
            && !new File(getDataFolder(), "data").isDirectory();
    if (legacy)
      try {
        Files.move(
            getDataFolder().toPath(),
            new File(getDataFolder().getParentFile(), "ZNPCsPlusLegacy").toPath());
      } catch (IOException e) {
        getLogger()
            .severe(ChatColor.RED + "Failed to move legacy data folder! Plugin will disable.");
        e.printStackTrace();
        Bukkit.getPluginManager().disablePlugin(this);
        return;
      }
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

  private static final Pattern EMBEDDED_FILE_PATTERN = Pattern.compile("\\{@(.*?)}");

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
}
