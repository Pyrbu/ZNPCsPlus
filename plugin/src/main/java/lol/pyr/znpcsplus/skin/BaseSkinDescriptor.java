package lol.pyr.znpcsplus.skin;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.skin.descriptor.FetchingDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.MirrorDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BaseSkinDescriptor extends SkinDescriptor {
    CompletableFuture<SkinImpl> fetch(Player player);
    SkinImpl fetchInstant(Player player);
    boolean supportsInstant(Player player);
    String serialize();

    static BaseSkinDescriptor deserialize(MojangSkinCache skinCache, String str) {
        String[] arr = str.split(";");
        if (arr[0].equalsIgnoreCase("mirror")) return new MirrorDescriptor(skinCache);
        else if (arr[0].equalsIgnoreCase("fetching")) return new FetchingDescriptor(skinCache, arr[1]);
        else if (arr[0].equalsIgnoreCase("prefetched")) {
            List<TextureProperty> properties = new ArrayList<>();
            for (int i = 0; i < (arr.length - 1) / 3; i++) {
                properties.add(new TextureProperty(arr[i + 1], arr[i + 2], arr[i + 3]));
            }
            return new PrefetchedDescriptor(new SkinImpl(properties));
        }
        throw new IllegalArgumentException("Unknown SkinDescriptor type!");
    }
}
