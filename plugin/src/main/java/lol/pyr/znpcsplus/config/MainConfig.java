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
    @ConfComments({"Should debug mode be enabled?", "This is used in development to test various things, you probably don't want to enable this"})
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
    @ConfComments("The ticks delay for NPCs to be hidden from the TAB")
    @DefaultInteger(60)
    int tabHideDelay();
}
