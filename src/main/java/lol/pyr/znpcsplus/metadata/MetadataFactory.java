package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.util.LazyLoader;

import java.util.HashMap;
import java.util.Map;

public interface MetadataFactory {
    EntityData skinLayers();

    MetadataFactory factory = get();

    static MetadataFactory get() {
        if (factory != null) return factory;
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        Map<ServerVersion, LazyLoader<? extends MetadataFactory>> factories = buildFactoryMap();
        if (factories.containsKey(version)) return factories.get(version).get();
        for (ServerVersion v : ServerVersion.reversedValues()) {
            if (v.isNewerThan(version)) continue;
            if (!factories.containsKey(v)) continue;
            return factories.get(v).get();
        }
        throw new RuntimeException("Unsupported version!");
    }

    private static Map<ServerVersion, LazyLoader<? extends MetadataFactory>> buildFactoryMap() {
        HashMap<ServerVersion, LazyLoader<? extends MetadataFactory>> map = new HashMap<>();
        map.put(ServerVersion.V_1_8, LazyLoader.of(V1_8Factory::new));
        map.put(ServerVersion.V_1_9, LazyLoader.of(V1_9Factory::new));
        map.put(ServerVersion.V_1_14, LazyLoader.of(V1_14Factory::new));
        map.put(ServerVersion.V_1_16, LazyLoader.of(V1_16Factory::new));
        map.put(ServerVersion.V_1_17, LazyLoader.of(V1_17Factory::new));
        return map;
    }
}
