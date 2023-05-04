package lol.pyr.znpcsplus.skin.descriptor;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.skin.Skin;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class FetchingDescriptor implements BaseSkinDescriptor, SkinDescriptor {
    private final String name;

    public FetchingDescriptor(String name) {
        this.name = name;
    }

    @Override
    public CompletableFuture<Skin> fetch(Player player) {
        return SkinCache.fetchByName(papi(player));
    }

    @Override
    public Skin fetchInstant(Player player) {
        return SkinCache.getFullyCachedByName(papi(player));
    }

    @Override
    public boolean supportsInstant(Player player) {
        return SkinCache.isNameFullyCached(papi(player));
    }

    private String papi(Player player) {
        if (ZNpcsPlus.PLACEHOLDERS_SUPPORTED) return PlaceholderAPI.setPlaceholders(player, name);
        return name;
    }
}
