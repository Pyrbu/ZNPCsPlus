package lol.pyr.znpcsplus.api;

public class NpcApiProvider {
    private static NpcApi plugin = null;

    private NpcApiProvider() {
        throw new UnsupportedOperationException();
    }

    public static NpcApi get() {
        if (plugin == null) throw new IllegalStateException(
                "ZNPCsPlus plugin isn't enabled yet!\n" +
                "Please add it to your plugin.yml as a depend or softdepend."
        );
        return plugin;
    }

    public static void register(NpcApi plugin) {
        NpcApiProvider.plugin = plugin;
    }

    public static void unregister() {
        NpcApiProvider.plugin = null;
    }
}
