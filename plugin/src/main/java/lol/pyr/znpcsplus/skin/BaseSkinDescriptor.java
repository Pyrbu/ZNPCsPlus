package lol.pyr.znpcsplus.skin;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.skin.descriptor.MirrorDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.NameFetchingDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.UUIDFetchingDescriptor;
import org.bukkit.entity.Player;

public interface BaseSkinDescriptor extends SkinDescriptor {
  CompletableFuture<SkinImpl> fetch(Player player);

  SkinImpl fetchInstant(Player player);

  boolean supportsInstant(Player player);

  String serialize();

  static BaseSkinDescriptor deserialize(MojangSkinCache skinCache, String str) {
    String[] arr = str.split(";");
    if (arr[0].equalsIgnoreCase("mirror")) return new MirrorDescriptor(skinCache);
    else if (arr[0].equalsIgnoreCase("fetching-uuid")) {
      String value = String.join(";", Arrays.copyOfRange(arr, 1, arr.length));
      return new UUIDFetchingDescriptor(skinCache, UUID.fromString(value));
    } else if (arr[0].equalsIgnoreCase("fetching")) {
      String value = String.join(";", Arrays.copyOfRange(arr, 1, arr.length));
      return new NameFetchingDescriptor(skinCache, value);
    } else if (arr[0].equalsIgnoreCase("prefetched")) {
      List<TextureProperty> properties = new ArrayList<>();
      for (int i = 0; i < (arr.length - 1) / 3; i++) {
        properties.add(new TextureProperty(arr[i + 1], arr[i + 2], arr[i + 3]));
      }
      return new PrefetchedDescriptor(new SkinImpl(properties));
    }
    throw new IllegalArgumentException("Unknown SkinDescriptor type!");
  }
}
