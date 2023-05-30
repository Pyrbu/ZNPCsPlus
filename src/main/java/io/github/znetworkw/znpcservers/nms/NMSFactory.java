package io.github.znetworkw.znpcservers.nms;

import com.google.common.collect.ImmutableSet;
import io.github.znetworkw.znpcservers.utility.Utils;

import java.util.Comparator;

public final class NMSFactory {
    public static final ImmutableSet<NMS> ALL = ImmutableSet.of(new NMSV8(), new NMSV9(), new NMSV16(), new NMSV17(), new NMSV18(), new NMSV19());

    public static final NMS NMS_FOR_CURRENT_VERSION = findPacketForVersion(Utils.BUKKIT_VERSION);

    public static NMS findPacketForVersion(int version) {
        return ALL.stream()
                .filter(NMS -> (version >= NMS.version()))
                .max(Comparator.comparing(NMS::version))
                .orElseThrow(() -> new IllegalArgumentException("No packet instance found for version: " + version));
    }
}
