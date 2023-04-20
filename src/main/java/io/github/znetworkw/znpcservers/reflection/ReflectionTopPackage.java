package io.github.znetworkw.znpcservers.reflection;

public enum ReflectionTopPackage {
    DEFAULT(""),
    NETWORK("network"),
    PROTOCOL("network.protocol"),
    CHAT("network.chat"),
    PACKET("network.protocol.game"),
    SYNCHER("network.syncher"),
    ENTITY("world.entity"),
    WORLD_ENTITY_PLAYER("world.entity.player"),
    ITEM("world.item"),
    WORLD_LEVEL("world.level"),
    WORLD_SCORES("world.scores"),
    SERVER_LEVEL("server.level"),
    SERVER_NETWORK("server.network"),
    SERVER("server");

    private final String packageName;

    ReflectionTopPackage(String subPackageName) {
        StringBuilder stringBuilder = new StringBuilder(ReflectionBasePackage.MINECRAFT_SERVER.getFixedPackageName());
        if (subPackageName.length() > 0) {
            stringBuilder.append(".");
            stringBuilder.append(subPackageName);
        }
        this.packageName = stringBuilder.toString();
    }

    public String getPackageName() {
        return this.packageName;
    }
}
