package io.github.znetworkw.znpcservers.npc;

import io.github.znetworkw.znpcservers.skin.SkinFetcherBuilder;
import io.github.znetworkw.znpcservers.skin.SkinFetcherResult;
import io.github.znetworkw.znpcservers.utility.Utils;

public class NPCSkin {
    private static final int LAYER_INDEX = SkinLayerValues.findLayerByVersion();
    private final String texture;
    private final String signature;

    protected NPCSkin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    public static NPCSkin forValues(String... values) {
        return new NPCSkin(values[0], (values.length > 1) ? values[1] : null);
    }

    public static void forName(String skin, SkinFetcherResult skinFetcherResult) {
        SkinFetcherBuilder.withName(skin).toSkinFetcher().doReadSkin(skinFetcherResult);
    }

    public String getTexture() {
        return this.texture;
    }

    public String getSignature() {
        return this.signature;
    }

    public int getLayerIndex() {
        return LAYER_INDEX;
    }

    enum SkinLayerValues {
        V8(8, 12),
        V9(10, 13),
        V14(14, 15),
        V16(15, 16),
        V17(17, 17),
        V18(18, 17);

        final int minVersion;

        final int layerValue;

        SkinLayerValues(int minVersion, int layerValue) {
            this.minVersion = minVersion;
            this.layerValue = layerValue;
        }

        static int findLayerByVersion() {
            int value = V8.layerValue;
            for (SkinLayerValues skinLayerValue : values()) {
                if (Utils.BUKKIT_VERSION >= skinLayerValue.minVersion)
                    value = skinLayerValue.layerValue;
            }
            return value;
        }
    }
}
