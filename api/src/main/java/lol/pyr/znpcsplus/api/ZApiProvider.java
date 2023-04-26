package lol.pyr.znpcsplus.api;

public class ZApiProvider {
    private static ZApi plugin = null;

    private ZApiProvider() {
        throw new UnsupportedOperationException();
    }

    public static ZApi get() {
        if (plugin == null) throw new IllegalStateException(
                "ZNPCsPlus plugin isn't enabled yet!\n" +
                "Please add it to your plugin.yml as a depend or softdepend."
        );
        return plugin;
    }

    public static void register(ZApi plugin) {
        ZApiProvider.plugin = plugin;
    }

    public static void unregister() {
        ZApiProvider.plugin = null;
    }
}
