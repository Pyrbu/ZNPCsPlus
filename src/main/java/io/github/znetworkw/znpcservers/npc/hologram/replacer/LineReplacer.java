package io.github.znetworkw.znpcservers.npc.hologram.replacer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;

public interface LineReplacer {
    ImmutableList<LineReplacer> LINE_REPLACERS = ImmutableList.of(new RGBLine());

    static String makeAll(ZUser user, String string) {
        for (UnmodifiableIterator<LineReplacer> unmodifiableIterator = LINE_REPLACERS.iterator(); unmodifiableIterator.hasNext(); ) {
            LineReplacer lineReplacer = unmodifiableIterator.next();
            if (!lineReplacer.isSupported())
                continue;
            string = lineReplacer.make(string);
        }
        return Utils.toColor((Utils.PLACEHOLDER_SUPPORT && user != null) ?
                Utils.getWithPlaceholders(string, user.toPlayer()) :
                string);
    }

    String make(String paramString);

    boolean isSupported();
}
