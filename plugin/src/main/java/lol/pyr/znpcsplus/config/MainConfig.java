package lol.pyr.znpcsplus.config;

import lol.pyr.znpcsplus.storage.NpcStorageType;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

import static space.arim.dazzleconf.annote.ConfDefault.*;

public interface MainConfig {
    @ConfKey("view-distance")
    @ConfComments("How far away do you need to be from any NPC for it to disappear, measured in blocks")
    @DefaultInteger(32)
    int viewDistance();

    @ConfKey("line-spacing")
    @ConfComments("The height between hologram lines, measured in blocks")
    @DefaultDouble(0.3D)
    double lineSpacing();

    @ConfKey("check-for-updates")
    @ConfComments("Should the plugin check for available updates and notify admins about them?")
    @DefaultBoolean(true)
    boolean checkForUpdates();

    @ConfKey("debug-enabled")
    @ConfComments({
            "Should debug mode be enabled?",
            "This is used in development to test various things, you probably don't want to enable this"
    })
    @DefaultBoolean(false)
    boolean debugEnabled();

    @ConfKey("storage-type")
    @ConfComments("The storage type to use. Available storage types: YAML, SQLITE, MYSQL")
    @DefaultString("YAML")
    NpcStorageType storageType();

    @ConfKey("database-config")
    @ConfComments("The database config. Only used if storage-type is MYSQL")
    @SubSection
    DatabaseConfig databaseConfig();

    @ConfKey("disable-skin-fetcher-warnings")
    @ConfComments("Set this to true if you don't want to be warned in the console when a skin fails to resolve")
    @DefaultBoolean(false)
    boolean disableSkinFetcherWarnings();

    @ConfKey("auto-save-interval")
    @ConfComments("How often to auto-save npcs, set this to -1 to disable. This value will only apply on restart")
    @DefaultInteger(300)
    int autoSaveInterval();

    default boolean autoSaveEnabled() {
        return autoSaveInterval() != -1;
    }

    @ConfKey("look-property-distance")
    @ConfComments("How far should the look property work from in blocks")
    @DefaultDouble(10)
    double lookPropertyDistance();
    
    @ConfKey("tab-hide-delay")
    @ConfComments({
            "The amount of time to wait before removing the npc from the player list (aka tab) in ticks",
            "If you're on 1.19.2 or above changing this value will have almost no effect since npcs are hidden in tab",
            "WARNING: Setting this value too low may cause issues with player npcs spawning"
    })
    @DefaultInteger(60)
    int tabHideDelay();

    @ConfKey("tab-display-name")
    @ConfComments("The display name to use for npcs in the player list (aka tab)")
    @DefaultString("ZNPC[{id}]")
    String tabDisplayName();
}
