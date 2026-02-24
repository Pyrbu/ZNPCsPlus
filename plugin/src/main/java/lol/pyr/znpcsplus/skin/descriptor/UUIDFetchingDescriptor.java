package lol.pyr.znpcsplus.skin.descriptor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.skin.SkinImpl;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import org.bukkit.entity.Player;

public class UUIDFetchingDescriptor implements BaseSkinDescriptor, SkinDescriptor {

  private final MojangSkinCache skinCache;
  private final UUID uuid;

  public UUIDFetchingDescriptor(MojangSkinCache skinCache, UUID uuid) {
    this.skinCache = skinCache;
    this.uuid = uuid;
  }

  @Override
  public CompletableFuture<SkinImpl> fetch(Player player) {
    return skinCache.fetchByUUID(uuid.toString());
  }

  @Override
  public SkinImpl fetchInstant(Player player) {
    return fetch(player).join();
  }

  @Override
  public boolean supportsInstant(Player player) {
    return false;
  }

  @Override
  public String serialize() {
    return "fetching-uuid;" + uuid.toString();
  }
}
