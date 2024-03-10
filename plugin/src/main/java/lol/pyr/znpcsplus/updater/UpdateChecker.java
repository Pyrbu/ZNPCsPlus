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

        status = compareVersions(info.getVersion(), newestVersion);
        if (status == Status.UPDATE_NEEDED) notifyConsole();
    }

    private void notifyConsole() {
        logger.warning("Version " + getLatestVersion() + " of " + info.getName() + " is available now!");
        logger.warning("Download it at " + UpdateChecker.DOWNLOAD_LINK);
    }

    private Status compareVersions(String currentVersion, String newVersion) {
        if (currentVersion.equalsIgnoreCase(newVersion)) return Status.LATEST_VERSION;
        ReleaseType currentType = parseReleaseType(currentVersion);
        ReleaseType newType = parseReleaseType(newVersion);
        if (currentType == ReleaseType.UNKNOWN || newType == ReleaseType.UNKNOWN) return Status.UNKNOWN;
        String currentVersionWithoutType = getVersionWithoutReleaseType(currentVersion);
        String newVersionWithoutType = getVersionWithoutReleaseType(newVersion);
        String[] currentParts = currentVersionWithoutType.split("\\.");
        String[] newParts = newVersionWithoutType.split("\\.");
        for (int i = 0; i < Math.min(currentParts.length, newParts.length); i++) {
            int currentPart = Integer.parseInt(currentParts[i]);
            int newPart = Integer.parseInt(newParts[i]);
            if (newPart > currentPart) return Status.UPDATE_NEEDED;
        }
        if (newType.ordinal() > currentType.ordinal()) return Status.UPDATE_NEEDED;
        if (newType == currentType) {
            int currentReleaseTypeNumber = getReleaseTypeNumber(currentVersion);
            int newReleaseTypeNumber = getReleaseTypeNumber(newVersion);
            if (newReleaseTypeNumber > currentReleaseTypeNumber) return Status.UPDATE_NEEDED;
        }
        return Status.LATEST_VERSION;
    }

    private ReleaseType parseReleaseType(String version) {
        if (version.toLowerCase().contains("snapshot")) return ReleaseType.SNAPSHOT;
        if (version.toLowerCase().contains("alpha")) return ReleaseType.ALPHA;
        if (version.toLowerCase().contains("beta")) return ReleaseType.BETA;
        return version.matches("\\d+\\.\\d+\\.\\d+") ? ReleaseType.RELEASE : ReleaseType.UNKNOWN;
    }

    private String getVersionWithoutReleaseType(String version) {
        return version.contains("-") ? version.split("-")[0] : version;
    }

    private int getReleaseTypeNumber(String version) {
        if (!version.contains("-")) return 0;
        return Integer.parseInt(version.split("-")[1].split("\\.")[1]);
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

    public enum ReleaseType {
        UNKNOWN, SNAPSHOT, ALPHA, BETA, RELEASE
    }
}
