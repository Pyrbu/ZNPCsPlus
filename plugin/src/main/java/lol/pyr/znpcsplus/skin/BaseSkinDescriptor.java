package lol.pyr.znpcsplus.skin;

import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface BaseSkinDescriptor {
    CompletableFuture<Skin> fetch(Player player);
    Skin fetchInstant(Player player);
    boolean supportsInstant(Player player);
}
