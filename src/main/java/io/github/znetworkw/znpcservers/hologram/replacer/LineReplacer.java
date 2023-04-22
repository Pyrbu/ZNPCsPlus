package io.github.znetworkw.znpcservers.hologram.replacer;

import com.google.common.collect.ImmutableList;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;

public interface LineReplacer {
    ImmutableList<LineReplacer> LINE_REPLACERS = ImmutableList.of(new RGBLine());

    static String makeAll(ZUser user, String string) {
        for (LineReplacer lineReplacer : LINE_REPLACERS) {
            if (!lineReplacer.isSupported()) continue;
            string = lineReplacer.make(string);
        }
        return Utils.toColor((Utils.PLACEHOLDER_SUPPORT && user != null) ? Utils.getWithPlaceholders(string, user.toPlayer()) : string);
    }

    String make(String paramString);

    boolean isSupported();
}
