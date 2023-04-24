package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.entity.PacketPlayer;
import lol.pyr.znpcsplus.util.LazyLoader;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public interface PacketFactory {
    void spawnPlayer(Player player, PacketPlayer entity);
    void spawnEntity(Player player, PacketEntity entity);
    void destroyEntity(Player player, PacketEntity entity);
    void teleportEntity(Player player, PacketEntity entity);
    void addTabPlayer(Player player, PacketPlayer entity);
    void removeTabPlayer(Player player, PacketPlayer entity);
    void createTeam(Player player, PacketPlayer entity);
    void removeTeam(Player player, PacketPlayer entity);

    PacketFactory factory = get();

    static PacketFactory get() {
        if (factory != null) return factory;
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        Map<ServerVersion, LazyLoader<? extends PacketFactory>> factories = buildFactoryMap();
        if (factories.containsKey(version)) return factories.get(version).get();
        for (ServerVersion v : ServerVersion.reversedValues()) {
            if (v.isNewerThan(version)) continue;
            if (!factories.containsKey(v)) continue;
            return factories.get(v).get();
        }
        throw new RuntimeException("Unsupported version!");
    }

    private static Map<ServerVersion, LazyLoader<? extends PacketFactory>> buildFactoryMap() {
        HashMap<ServerVersion, LazyLoader<? extends PacketFactory>> map = new HashMap<>();
        map.put(ServerVersion.V_1_8, LazyLoader.of(V1_8Factory::new));
        map.put(ServerVersion.V_1_14, LazyLoader.of(V1_14Factory::new));
        map.put(ServerVersion.V_1_19, LazyLoader.of(V1_19Factory::new));
        return map;
    }
}
