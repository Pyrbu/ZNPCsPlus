package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.util.LazyLoader;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface PacketFactory {
    void spawnPlayer(Player player, PacketEntity entity, PropertyHolder properties);
    void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties);
    void destroyEntity(Player player, PacketEntity entity, PropertyHolder properties);
    void teleportEntity(Player player, PacketEntity entity);
    CompletableFuture<Void> addTabPlayer(Player player, PacketEntity entity, PropertyHolder properties);
    void removeTabPlayer(Player player, PacketEntity entity);
    void createTeam(Player player, PacketEntity entity, PropertyHolder properties);
    void removeTeam(Player player, PacketEntity entity);
    void sendAllMetadata(Player player, PacketEntity entity, PropertyHolder properties);
    void sendMetadata(Player player, PacketEntity entity, List<EntityData> data);

    PacketFactory factory = get();

    static PacketFactory get() {
        if (factory != null) return factory;
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        Map<ServerVersion, LazyLoader<? extends PacketFactory>> factories = buildFactoryMap();
        if (factories.containsKey(version)) return factories.get(version).get();
        for (ServerVersion v : ServerVersion.reversedValues()) {
            if (v.isNewerThan(version)) continue;
            if (!factories.containsKey(v)) continue;
            PacketFactory f = factories.get(v).get();
            ZNpcsPlus.debug("Using PacketFactory Version " + v.name() + " (" + f.getClass().getName() + ")");
            return f;
        }
        throw new RuntimeException("Unsupported version!");
    }

    static Map<ServerVersion, LazyLoader<? extends PacketFactory>> buildFactoryMap() {
        HashMap<ServerVersion, LazyLoader<? extends PacketFactory>> map = new HashMap<>();
        map.put(ServerVersion.V_1_8, LazyLoader.of(V1_8Factory::new));
        map.put(ServerVersion.V_1_9, LazyLoader.of(V1_9Factory::new));
        map.put(ServerVersion.V_1_14, LazyLoader.of(V1_14Factory::new));
        map.put(ServerVersion.V_1_19, LazyLoader.of(V1_19Factory::new));
        return map;
    }
}
