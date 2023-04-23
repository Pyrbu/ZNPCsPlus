package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import lol.pyr.znpcsplus.util.LazyLoader;

import java.util.Map;

public interface PacketFactory {

    Map<ServerVersion, LazyLoader<? extends PacketFactory>> factories = buildFactoryMap();


    static PacketFactory get() {
        return factories.get(PacketEvents.getAPI().getServerManager().getVersion()).get();
    }

    private static Map<ServerVersion, LazyLoader<? extends PacketFactory>> buildFactoryMap() {

    }
}
