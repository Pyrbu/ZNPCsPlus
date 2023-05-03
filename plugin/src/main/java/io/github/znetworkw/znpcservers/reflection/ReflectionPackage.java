package io.github.znetworkw.znpcservers.reflection;

import io.github.znetworkw.znpcservers.utility.VersionUtil;

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
    public static final String MINECRAFT = join("net.minecraft", flattened ? "server." + VersionUtil.getBukkitPackage() : "");
    public static final String ENTITY = flattened ? MINECRAFT : join(MINECRAFT, "world.entity");

    // Simple method that joins all the non-null & non-empty arguments with a dot and returns the result
    public static String join(String... parts) {
        return Arrays.stream(parts)
                .filter(Objects::nonNull)
                .filter(p -> p.length() != 0)
                .collect(Collectors.joining("."));
    }
}
