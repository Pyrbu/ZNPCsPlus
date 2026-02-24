package lol.pyr.znpcsplus.api.skin;

import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public interface SkinDescriptor {
  CompletableFuture<? extends Skin> fetch(Player player);

  Skin fetchInstant(Player player);

  boolean supportsInstant(Player player);
}
