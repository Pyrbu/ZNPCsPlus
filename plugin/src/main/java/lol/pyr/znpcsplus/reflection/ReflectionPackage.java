package lol.pyr.znpcsplus.reflection;

import lol.pyr.znpcsplus.util.VersionUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A class containing all of the packages of the server jar that we import classes from.
 * Every line has a check for the "flattened" variable due to the fact that server jars
 * pre-1.17 had all of their classes "flattened" into one package.
 */
public class ReflectionPackage {
    private static final boolean flattened = !VersionUtil.isNewerThan(17);
    public static final String BUKKIT = "org.bukkit.craftbukkit." + VersionUtil.getBukkitPackage();

    /**
     * Check if the classes are flattened, if so we need to add the version string into the
     * package string which is another quirk of the old server jars.
     */
    public static final String MINECRAFT = joinWithDot("net.minecraft", flattened ? "server." + VersionUtil.getBukkitPackage() : "");
    public static final String ENTITY = flattened ? MINECRAFT : joinWithDot(MINECRAFT, "world.entity");

    public static String joinWithDot(String... parts) {
        return Arrays.stream(parts)
                .filter(Objects::nonNull)
                .filter(p -> p.length() != 0)
                .collect(Collectors.joining("."));
    }
}
