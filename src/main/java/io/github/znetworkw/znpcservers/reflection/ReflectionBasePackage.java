package io.github.znetworkw.znpcservers.reflection;

import io.github.znetworkw.znpcservers.utility.Utils;

public enum ReflectionBasePackage {
    DEFAULT,
    CRAFT_BUKKIT("org.bukkit.craftbukkit." + Utils.getBukkitPackage()),
    MINECRAFT_SERVER("net.minecraft");

    private final String fixedPackageName;

    ReflectionBasePackage(String packageName) {
        this.fixedPackageName = Utils.versionNewer(17) ? packageName : (packageName + (packageName.contains("minecraft") ? (".server." + Utils.getBukkitPackage()) : ""));
    }

    ReflectionBasePackage() {
        this.fixedPackageName = "";
    }

    public String getForCategory(ReflectionTopPackage packetCategory, String extra) {
        return Utils.versionNewer(17) ? (packetCategory
                .getPackageName() + ((extra.length() > 0) ? ("." + extra) : "")) :
                this.fixedPackageName;
    }

    public String getFixedPackageName() {
        return this.fixedPackageName;
    }
}
