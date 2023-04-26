package lol.pyr.znpcsplus.config;

import space.arim.dazzleconf.annote.ConfKey;

import static space.arim.dazzleconf.annote.ConfDefault.*;

// TODO: Add comments to the values using @ConfComments()
public interface MainConfig {
    @ConfKey("view-distance")
    @DefaultInteger(32)
    int viewDistance();

    @ConfKey("line-spacing")
    @DefaultDouble(0.3D)
    double lineSpacing();

    @ConfKey("debug-enabled")
    @DefaultBoolean(false)
    boolean debugEnabled();

    @ConfKey("check-for-updates")
    @DefaultBoolean(true)
    boolean checkForUpdates();
}
