package io.github.znetworkw.znpcservers.skin;

import io.github.znetworkw.znpcservers.npc.NPCSkin;

public interface SkinFetcherResult {
    void onDone(NPCSkin npcSkin, Throwable paramThrowable);
}
