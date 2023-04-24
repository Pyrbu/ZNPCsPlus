package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import io.github.znetworkw.znpcservers.npc.NPCSkin;
import lol.pyr.znpcsplus.entity.PacketPlayer;

import java.util.List;

public class NPCPropertyKey<T> {
    private final UpdateCallback<T> updateCallback;

    public NPCPropertyKey() {
        this(null);
    }

    public NPCPropertyKey(UpdateCallback<T> updateCallback) {
        this.updateCallback = updateCallback;
    }

    public void update(NPC npc, T value) {
        if (updateCallback != null) updateCallback.onUpdate(npc, value);
    }

    @FunctionalInterface
    public interface UpdateCallback<T> {
        void onUpdate(NPC npc, T value);
    }

    public static NPCPropertyKey<NPCSkin> NPC_SKIN = new NPCPropertyKey<>((npc, skin) -> {
        if (!(npc.getEntity() instanceof PacketPlayer entity))
            throw new RuntimeException("Tried to set a skin on an entity that isn't a player");
        entity.getGameProfile().setTextureProperties(List.of(new TextureProperty("textures", skin.getTexture(), skin.getSignature())));
        npc.respawn();
    });
}
