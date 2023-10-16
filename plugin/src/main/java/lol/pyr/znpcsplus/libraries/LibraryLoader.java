package lol.pyr.znpcsplus.libraries;

import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class LibraryLoader {
    private final static Logger logger = Logger.getLogger("ZNPCsPlus Library Loader");

    private final UrlClassLoaderAccess loaderAccess;
    private final File librariesFolder;
    private final Set<File> loadedLibraries = new HashSet<>();
    private final List<Relocation> relocationRules = new ArrayList<>();

    public LibraryLoader(Plugin plugin, File librariesFolder) {
        loaderAccess = UrlClassLoaderAccess.create((URLClassLoader) plugin.getClass().getClassLoader());
        this.librariesFolder = librariesFolder;
        if (!librariesFolder.exists()) librariesFolder.mkdirs();
    }

    public void deleteUnloadedLibraries() {
        File[] files = librariesFolder.listFiles();
        if (files == null) return;
        for (File file : files) if (!loadedLibraries.contains(file)) file.delete();
    }

    public void addRelocation(String pre, String post) {
        relocationRules.add(new Relocation(pre, post));
    }

    public void loadSnapshotLibrary(String groupId, String artifactId, String version, String snapshotVersion, String repoUrl) {
        try {
            loadLibrary(groupId + ":" + artifactId + ":" + version,
                    getDependencyFile(groupId, artifactId, version),
                    getSnapshotDependencyUrl(groupId, artifactId, version, snapshotVersion, repoUrl));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public int loadedLibraryCount() {
        return loadedLibraries.size();
    }

    public void loadLibrary(String groupId, String artifactId, String version) {
        loadLibrary(groupId, artifactId, version, "https://repo1.maven.org/maven2");
    }

    public void loadLibrary(String groupId, String artifactId, String version, String repoUrl) {
        try {
            loadLibrary(groupId + ":" + artifactId + ":" + version,
                    getDependencyFile(groupId, artifactId, version),
                    getDependencyUrl(groupId, artifactId, version, repoUrl));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadLibrary(String name, File file, URL url) {
        if (!file.exists()) {
            try (InputStream in = url.openStream()) {
                File temp = new File(file.getParentFile(), file.getName() + ".temp");
                Files.copy(in, temp.toPath());
                new JarRelocator(temp, file, relocationRules).run();
                temp.delete();
                // logger.info("Downloaded library " + name);
            } catch (IOException e) {
                logger.severe("Failed to download library " + name);
                e.printStackTrace();
            }
        }

        try {
            loaderAccess.addURL(file.toURI().toURL());
            loadedLibraries.add(file);
            // logger.info("Loaded library " + name);
        } catch (Exception e) {
            logger.severe("Failed to load library, plugin may not work correctly (" + name + ")");
            e.printStackTrace();
        }
    }

    private File getDependencyFile(String groupId, String artifactId, String version) {
        return new File(librariesFolder, groupId.replace(".", "-") + "-"
                + artifactId.replace(".", "-") + "-"
                + version.replace(".", "-") + ".jar");
    }

    private static URL getDependencyUrl(String groupId, String artifactId, String version, String repoUrl) throws MalformedURLException {
        String url = repoUrl.endsWith("/") ? repoUrl : repoUrl + "/";
        url += groupId.replace(".", "/") + "/";
        url += artifactId + "/";
        url += version + "/";
        url += artifactId + "-" + version + ".jar";
        return new URL(url);
    }

    private static URL getSnapshotDependencyUrl(String groupId, String artifactId, String version, String snapshotVersion, String repoUrl) throws MalformedURLException {
        String url = repoUrl.endsWith("/") ? repoUrl : repoUrl + "/";
        url += groupId.replace(".", "/") + "/";
        url += artifactId + "/";
        url += version + "/";
        url += artifactId + "-" + snapshotVersion + ".jar";
        return new URL(url);
    }
}
