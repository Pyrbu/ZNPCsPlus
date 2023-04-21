package io.github.znetworkw.znpcservers.reflection;

import io.github.znetworkw.znpcservers.utility.Utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A class containing all of the packages of the server jar that we import classes from.
 * Every line has a check for the "flattened" variable due to the fact that server jars
 * pre-1.17 had all of their classes "flattened" into one package.
 */
public class ReflectionPackage {
    private static final boolean flattened = !Utils.versionNewer(17);

    public static final String BUKKIT = "org.bukkit.craftbukkit." + Utils.getBukkitPackage();

    /**
     * Check if the classes are flattened, if so we need to add the version string into the
     * package string which is another quirk of the old server jars.
     */
    public static final String MINECRAFT = join("net.minecraft", flattened ? "server." + Utils.getBukkitPackage() : "");

    public static final String NETWORK = flattened ? MINECRAFT : join(MINECRAFT, "network");
    public static final String PROTOCOL = flattened ? MINECRAFT : join(MINECRAFT, "network.protocol");
    public static final String CHAT = flattened ? MINECRAFT : join(MINECRAFT, "network.chat");
    public static final String PACKET = flattened ? MINECRAFT : join(MINECRAFT, "network.protocol.game");
    public static final String SYNCHER = flattened ? MINECRAFT : join(MINECRAFT, "network.syncher");
    public static final String ENTITY = flattened ? MINECRAFT : join(MINECRAFT, "world.entity");
    public static final String WORLD_ENTITY_PLAYER = flattened ? MINECRAFT : join(MINECRAFT, "world.entity.player");
    public static final String ITEM = flattened ? MINECRAFT : join(MINECRAFT, "world.item");
    public static final String WORLD_LEVEL = flattened ? MINECRAFT : join(MINECRAFT, "world.level");
    public static final String WORLD_SCORES = flattened ? MINECRAFT : join(MINECRAFT, "world.scores");
    public static final String SERVER_LEVEL = flattened ? MINECRAFT : join(MINECRAFT, "server.level");
    public static final String SERVER_NETWORK = flattened ? MINECRAFT : join(MINECRAFT, "server.network");
    public static final String SERVER = flattened ? MINECRAFT : join(MINECRAFT, "server");

    // Simple method that joins all the non-null & non-empty arguments with a dot and returns the result
    public static String join(String... parts) {
        return Arrays.stream(parts)
                .filter(Objects::nonNull)
                .filter(p -> p.length() != 0)
                .collect(Collectors.joining("."));
    }
}
