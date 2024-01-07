package lol.pyr.znpcsplus.api.skin;

import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface SkinDescriptor {
    CompletableFuture<? extends Skin> fetch(Player player);
    Skin fetchInstant(Player player);
    boolean supportsInstant(Player player);
}
