package lol.pyr.znpcsplus.commands;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.ZNpcsPlus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class VersionCommand implements CommandHandler {

  private final String pluginVersion;
  private final String gitBranch;
  private final String gitCommitHash;
  private final String buildId;

  public VersionCommand(ZNpcsPlus plugin) {
    pluginVersion = plugin.getDescription().getVersion();
    String gitBranch = "";
    String gitCommitHash = "";
    String buildId = "";
    try {
      URL jarUrl = getClass().getProtectionDomain().getCodeSource().getLocation();
      JarFile jarFile = new JarFile(jarUrl.toURI().getPath());
      Attributes attributes = jarFile.getManifest().getMainAttributes();
      gitBranch = attributes.getValue("Git-Branch");
      gitCommitHash = attributes.getValue("Git-Commit-Hash");
      buildId = attributes.getValue("Build-Id");
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
    this.gitBranch = gitBranch;
    this.gitCommitHash = gitCommitHash;
    this.buildId = buildId;
  }

  @Override
  public void run(CommandContext context) throws CommandExecutionException {

    StringBuilder versionBuilder =
        new StringBuilder("This server is running ZNPCsPlus version ").append(pluginVersion);
    if (gitBranch != null && !gitBranch.isEmpty()) {
      versionBuilder.append("-").append(gitBranch);
    }
    if (gitCommitHash != null && !gitCommitHash.isEmpty()) {
      versionBuilder.append("@").append(gitCommitHash);
    }
    if (buildId != null && !buildId.isEmpty()) {
      versionBuilder.append(" (Build #").append(buildId).append(")");
    } else {
      versionBuilder.append(" (Development Build)");
    }

    String version = versionBuilder.toString();

    context.send(
        Component.text(version, NamedTextColor.GREEN)
            .hoverEvent(Component.text("Click to copy version to clipboard"))
            .clickEvent(ClickEvent.copyToClipboard(version)));
  }
}
