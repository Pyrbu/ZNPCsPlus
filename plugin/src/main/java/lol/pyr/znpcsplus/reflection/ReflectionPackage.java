package lol.pyr.znpcsplus.reflection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A class containing getAll of the packages of the server jar that we import classes from.
 * Every line has a check for the "flattened" variable due to the fact that server jars
 * pre-1.17 had all of their classes "flattened" into one package.
 */
public class ReflectionPackage {
    private static final String VERSION = generateVersion();
    public static final String BUKKIT = "org.bukkit.craftbukkit" + VERSION;
    private static final boolean flattened = !PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_17);

    /**
     * Check if the classes are flattened, if so we need to add the version string into the
     * package string which is another quirk of the old server jars.
     */
    public static final String MINECRAFT = joinWithDot("net.minecraft", flattened ? "server" + VERSION : "");
    public static final String ENTITY = flattened ? MINECRAFT : joinWithDot(MINECRAFT, "world.entity");
    public static final String SERVER_LEVEL = flattened ? MINECRAFT : joinWithDot(MINECRAFT, "server.level");

    public static String joinWithDot(String... parts) {
        return Arrays.stream(parts)
                .filter(Objects::nonNull)
                .filter(p -> !p.isEmpty())
                .collect(Collectors.joining("."));
    }

    private static String generateVersion() {
        String[] parts = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        if (parts.length > 3) return "." + parts[3];
        return "";
    }
}
