package lol.pyr.znpcsplus.skin.descriptor;

import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.skin.Skin;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class MirrorDescriptor implements BaseSkinDescriptor, SkinDescriptor {

    public MirrorDescriptor() {}

    @Override
    public CompletableFuture<Skin> fetch(Player player) {
        return CompletableFuture.completedFuture(SkinCache.getFromPlayer(player));
    }

    @Override
    public Skin fetchInstant(Player player) {
        return SkinCache.getFromPlayer(player);
    }

    @Override
    public boolean supportsInstant(Player player) {
        return true;
    }
}
