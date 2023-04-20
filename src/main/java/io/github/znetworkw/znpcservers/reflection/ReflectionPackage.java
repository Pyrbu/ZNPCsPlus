package io.github.znetworkw.znpcservers.reflection;

import io.github.znetworkw.znpcservers.utility.Utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReflectionPackage {
    public static final String BUKKIT = fixBasePackage("org.bukkit.craftbukkit." + Utils.getBukkitPackage());
    public static final String MINECRAFT = fixBasePackage("net.minecraft");

    public static final String NETWORK = join(MINECRAFT, "network");
    public static final String PROTOCOL = join(MINECRAFT, "network.protocol");
    public static final String CHAT = join(MINECRAFT, "network.chat");
    public static final String PACKET = join(MINECRAFT, "network.protocol.game");
    public static final String SYNCHER = join(MINECRAFT, "network.syncher");
    public static final String ENTITY = join(MINECRAFT, "world.entity");
    public static final String WORLD_ENTITY_PLAYER = join(MINECRAFT, "world.entity.player");
    public static final String ITEM = join(MINECRAFT, "world.item");
    public static final String WORLD_LEVEL = join(MINECRAFT, "world.level");
    public static final String WORLD_SCORES = join(MINECRAFT, "world.scores");
    public static final String SERVER_LEVEL = join(MINECRAFT, "server.level");
    public static final String SERVER_NETWORK = join(MINECRAFT, "server.network");
    public static final String SERVER = join(MINECRAFT, "server");

    public static String join(String... parts) {
        return Arrays.stream(parts)
                .filter(Objects::nonNull)
                .filter(p -> p.length() != 0)
                .collect(Collectors.joining("."));
    }

    private static String fixBasePackage(String packageName) {
        return Utils.versionNewer(17) ? packageName : (packageName + (packageName.contains("minecraft") ? (".server." + Utils.getBukkitPackage()) : ""));
    }
}
