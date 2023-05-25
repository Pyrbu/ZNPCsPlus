package lol.pyr.znpcsplus.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public class NpcApiProvider {
    private static NpcApi api = null;

    private NpcApiProvider() {
        throw new UnsupportedOperationException();
    }

    public static NpcApi get() {
        if (api == null) throw new IllegalStateException(
                "ZNPCsPlus plugin isn't enabled yet!\n" +
                "Please add it to your plugin.yml as a depend or softdepend."
        );
        return api;
    }

    public static void register(Plugin plugin, NpcApi api) {
        NpcApiProvider.api = api;
        Bukkit.getServicesManager().register(NpcApi.class, api, plugin, ServicePriority.Normal);
    }

    public static void unregister() {
        Bukkit.getServicesManager().unregister(api);
        NpcApiProvider.api = null;
    }
}
