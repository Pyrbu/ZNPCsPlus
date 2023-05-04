package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.util.LazyLoader;
import net.kyori.adventure.text.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 1.8  https://wiki.vg/index.php?title=Entity_metadata&oldid=7415
 * 1.9  https://wiki.vg/index.php?title=Entity_metadata&oldid=7968
 * 1.10 https://wiki.vg/index.php?title=Entity_metadata&oldid=8241
 * 1.11 https://wiki.vg/index.php?title=Entity_metadata&oldid=8534
 * 1.12 https://wiki.vg/index.php?title=Entity_metadata&oldid=14048
 * 1.13 https://wiki.vg/index.php?title=Entity_metadata&oldid=14800
 * 1.14 https://wiki.vg/index.php?title=Entity_metadata&oldid=15240
 * 1.15 https://wiki.vg/index.php?title=Entity_metadata&oldid=15991
 * 1.16 https://wiki.vg/index.php?title=Entity_metadata&oldid=16539
 * 1.17 https://wiki.vg/index.php?title=Entity_metadata&oldid=17521
 * 1.18 NOTHING CHANGED
 * 1.19 https://wiki.vg/index.php?title=Entity_metadata
 */
public interface MetadataFactory {
    EntityData skinLayers();
    EntityData effects(boolean onFire, boolean glowing, boolean invisible);
    Collection<EntityData> name(Component name);
    EntityData silent();

    MetadataFactory factory = get();

    static MetadataFactory get() {
        if (factory != null) return factory;
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        Map<ServerVersion, LazyLoader<? extends MetadataFactory>> factories = buildFactoryMap();
        if (factories.containsKey(version)) return factories.get(version).get();
        for (ServerVersion v : ServerVersion.reversedValues()) {
            if (v.isNewerThan(version)) continue;
            if (!factories.containsKey(v)) continue;
            MetadataFactory f = factories.get(v).get();
            ZNpcsPlus.debug("Using MetadataFactory Version " + v.name() + " (" + f.getClass().getName() + ")");
            return f;
        }
        throw new RuntimeException("Unsupported version!");
    }

    static Map<ServerVersion, LazyLoader<? extends MetadataFactory>> buildFactoryMap() {
        HashMap<ServerVersion, LazyLoader<? extends MetadataFactory>> map = new HashMap<>();
        map.put(ServerVersion.V_1_8, LazyLoader.of(V1_8Factory::new));
        map.put(ServerVersion.V_1_9, LazyLoader.of(V1_9Factory::new));
        map.put(ServerVersion.V_1_13, LazyLoader.of(V1_13Factory::new));
        map.put(ServerVersion.V_1_14, LazyLoader.of(V1_14Factory::new));
        map.put(ServerVersion.V_1_16, LazyLoader.of(V1_16Factory::new));
        map.put(ServerVersion.V_1_17, LazyLoader.of(V1_17Factory::new));
        return map;
    }
}
