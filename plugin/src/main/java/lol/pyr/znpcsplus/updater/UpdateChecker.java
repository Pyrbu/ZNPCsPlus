package lol.pyr.znpcsplus.updater;

import me.robertlit.spigotresources.api.Resource;
import me.robertlit.spigotresources.api.SpigotResourcesAPI;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UpdateChecker extends BukkitRunnable {
    private final static Logger logger = Logger.getLogger("ZNPCsPlus Update Checker");
    private final static int RESOURCE_ID = 109380;
    public final static String DOWNLOAD_LINK = "https://www.spigotmc.org/resources/znpcsplus.109380/";

    private final SpigotResourcesAPI api = new SpigotResourcesAPI(1, TimeUnit.MINUTES);

    private final PluginDescriptionFile info;
    private Status status = Status.UNKNOWN;
    private String newestVersion = "N/A";

    public UpdateChecker(PluginDescriptionFile info) {
        this.info = info;
    }

    public void run() {
        Resource resource = api.getResource(RESOURCE_ID).join();
        if (resource == null) return;
        newestVersion = resource.getVersion();

        int current = versionToNumber(info.getVersion());
        int newest = versionToNumber(newestVersion);

        status = current >= newest ? Status.LATEST_VERSION : Status.UPDATE_NEEDED;
        if (status == Status.UPDATE_NEEDED) notifyConsole();
    }

    private void notifyConsole() {
        logger.warning("Version " + getLatestVersion() + " of " + info.getName() + " is available now!");
        logger.warning("Download it at " + UpdateChecker.DOWNLOAD_LINK);
    }

    public void shutdown() {
        cancel();
    }

    private int versionToNumber(String version) {
        int num = Integer.parseInt(version.replaceAll("[^0-9]", ""));
        if (version.toLowerCase().contains("snapshot")) num -= 1;
        return num;
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
