package lol.pyr.znpcsplus.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

/**
 * Provider for the registered api instance
 */
public class NpcApiProvider {
    private static NpcApi api = null;

    private NpcApiProvider() {
        throw new UnsupportedOperationException();
    }

    /**
     * Static method that returns the api instance of the plugin
     *
     * @return The instance of the api for the ZNPCsPlus plugin
     */
    public static NpcApi get() {
        if (api == null) throw new IllegalStateException(
                "ZNPCsPlus plugin isn't enabled yet!\n" +
                "Please add it to your plugin.yml as a depend or softdepend."
        );
        return api;
    }

    /**
     * Internal method used to register the main instance of the plugin as the api provider
     * You probably shouldn't call this method under any circumstances
     *
     * @param plugin Instance of the ZNPCsPlus plugin
     * @param api Instance of the ZNPCsPlus api
     */
    public static void register(Plugin plugin, NpcApi api) {
        NpcApiProvider.api = api;
        Bukkit.getServicesManager().register(NpcApi.class, api, plugin, ServicePriority.Normal);
    }

    /**
     * Internal method used to unregister the plugin from the provider when the plugin shuts down
     * You probably shouldn't call this method under any circumstances
     */
    public static void unregister() {
        Bukkit.getServicesManager().unregister(api);
        NpcApiProvider.api = null;
    }
}
