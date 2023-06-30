package lol.pyr.znpcsplus.skin.descriptor;

import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.skin.Skin;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class MirrorDescriptor implements BaseSkinDescriptor, SkinDescriptor {
    private final MojangSkinCache skinCache;

    public MirrorDescriptor(MojangSkinCache skinCache) {
        this.skinCache = skinCache;
    }

    @Override
    public CompletableFuture<Skin> fetch(Player player) {
        return CompletableFuture.completedFuture(skinCache.getFromPlayer(player));
    }

    @Override
    public Skin fetchInstant(Player player) {
        return skinCache.getFromPlayer(player);
    }

    @Override
    public boolean supportsInstant(Player player) {
        return true;
    }

    @Override
    public String serialize() {
        return "mirror";
    }
}
