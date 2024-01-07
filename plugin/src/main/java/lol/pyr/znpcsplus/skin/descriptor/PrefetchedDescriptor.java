package lol.pyr.znpcsplus.skin.descriptor;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.skin.SkinImpl;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import org.bukkit.entity.Player;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class PrefetchedDescriptor implements BaseSkinDescriptor, SkinDescriptor {
    private final SkinImpl skin;

    public PrefetchedDescriptor(SkinImpl skin) {
        this.skin = skin;
    }

    public static CompletableFuture<PrefetchedDescriptor> forPlayer(MojangSkinCache cache, String name) {
        return CompletableFuture.supplyAsync(() -> new PrefetchedDescriptor(cache.fetchByName(name).join()));
    }

    public static CompletableFuture<PrefetchedDescriptor> fromUrl(MojangSkinCache cache, URL url, String variant) {
        return CompletableFuture.supplyAsync(() -> new PrefetchedDescriptor(cache.fetchByUrl(url, variant).join()));
    }

    @Override
    public CompletableFuture<SkinImpl> fetch(Player player) {
        return CompletableFuture.completedFuture(skin);
    }

    @Override
    public SkinImpl fetchInstant(Player player) {
        return skin;
    }

    @Override
    public boolean supportsInstant(Player player) {
        return true;
    }

    public SkinImpl getSkin() {
        return skin;
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("prefetched;");
        for (TextureProperty property : skin.getProperties()) {
            sb.append(property.getName()).append(";");
            sb.append(property.getValue()).append(";");
            sb.append(property.getSignature()).append(";");
        }
        return sb.toString();
    }
}
