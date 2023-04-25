package lol.pyr.znpcsplus.skin.descriptor;

import lol.pyr.znpcsplus.skin.Skin;
import lol.pyr.znpcsplus.skin.SkinDescriptor;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PrefetchedDescriptor implements SkinDescriptor {
    private final Skin skin;

    public PrefetchedDescriptor(Skin skin) {
        this.skin = skin;
    }

    @Override
    public CompletableFuture<Skin> fetch(Player player) {
        return CompletableFuture.completedFuture(skin);
    }

    @Override
    public Skin fetchInstant(Player player) {
        return skin;
    }

    @Override
    public boolean supportsInstant(Player player) {
        return true;
    }
}
