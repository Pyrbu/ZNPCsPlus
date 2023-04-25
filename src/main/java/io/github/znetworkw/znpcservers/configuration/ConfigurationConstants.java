package io.github.znetworkw.znpcservers.configuration;

public final class ConfigurationConstants {
    public static final String SPACE_SYMBOL = Configuration.CONFIGURATION.getValue(ConfigurationValue.REPLACE_SYMBOL);
    public static final boolean DEBUG_ENABLED = Configuration.CONFIGURATION.getValue(ConfigurationValue.DEBUG_ENABLED);
    public static final int VIEW_DISTANCE = Configuration.CONFIGURATION.<Integer>getValue(ConfigurationValue.VIEW_DISTANCE);
    public static final boolean CHECK_FOR_UPDATES = Configuration.CONFIGURATION.getValue(ConfigurationValue.CHECK_FOR_UPDATES);
}
