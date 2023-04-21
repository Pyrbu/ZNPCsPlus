package lol.pyr.znpcsplus.updater;

import lol.pyr.znpcsplus.ZNPCsPlus;
import me.robertlit.spigotresources.api.Resource;
import me.robertlit.spigotresources.api.SpigotResourcesAPI;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class UpdateChecker extends BukkitRunnable {
    private final static SpigotResourcesAPI api = new SpigotResourcesAPI(1, TimeUnit.MINUTES);
    public final static int RESOURCE_ID = 109380;
    public final static String DOWNLOAD_LINK = "https://www.spigotmc.org/resources/znpcsplus.109380/";

    private final ZNPCsPlus plugin;
    private Status status = Status.UNKNOWN;
    private String newestVersion = "N/A";

    public UpdateChecker(ZNPCsPlus plugin) {
        this.plugin = plugin;
        runTaskTimerAsynchronously(plugin, 5L, 6000L);
    }

    public void run() {
        Resource resource = api.getResource(RESOURCE_ID).join();
        newestVersion = resource.getVersion();

        int current = versionToNumber(plugin.getDescription().getVersion());
        int newest = versionToNumber(newestVersion);

        status = current >= newest ? Status.LATEST_VERSION : Status.UPDATE_NEEDED;
        if (status == Status.UPDATE_NEEDED) notifyConsole();
    }

    private void notifyConsole() {
        ZNPCsPlus.LOGGER.warning("Version " + getLatestVersion() + " of " + plugin.getDescription().getName() + " is available now!");
        ZNPCsPlus.LOGGER.warning("Download it at " + UpdateChecker.DOWNLOAD_LINK);
    }

    private int versionToNumber(String version) {
        return Integer.parseInt(version.replaceAll("\\.", ""));
    }

    public Status getStatus() {
        return status;
    }

    public String getLatestVersion() {
        return newestVersion;
    }

    public enum Status {
        UNKNOWN, LATEST_VERSION, UPDATE_NEEDED
    }
}
