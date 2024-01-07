package lol.pyr.znpcsplus.skin.descriptor;

import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.skin.SkinImpl;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.util.PapiUtil;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class FetchingDescriptor implements BaseSkinDescriptor, SkinDescriptor {
    private final MojangSkinCache skinCache;
    private final String name;

    public FetchingDescriptor(MojangSkinCache skinCache, String name) {
        this.skinCache = skinCache;
        this.name = name;
    }

    @Override
    public CompletableFuture<SkinImpl> fetch(Player player) {
        return skinCache.fetchByName(PapiUtil.set(player, name));
    }

    @Override
    public SkinImpl fetchInstant(Player player) {
        return skinCache.getFullyCachedByName(PapiUtil.set(player, name));
    }

    @Override
    public boolean supportsInstant(Player player) {
        return skinCache.isNameFullyCached(PapiUtil.set(player, name));
    }

    public String getName() {
        return name;
    }

    @Override
    public String serialize() {
        return "fetching;" + name;
    }
}
