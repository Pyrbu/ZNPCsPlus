package lol.pyr.znpcsplus.config;

import space.arim.dazzleconf.ConfigurationFactory;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.error.ConfigFormatSyntaxException;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.snakeyaml.CommentMode;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlOptions;
import space.arim.dazzleconf.helper.ConfigurationHelper;
import space.arim.dazzleconf.serialiser.ValueSerialiser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfigManager {
    private final static Logger logger = Logger.getLogger("ZNPCsPlus Configuration Manager");

    private volatile MainConfig config;
    private final ConfigurationHelper<MainConfig> configHelper;

    private volatile MessageConfig messages;
    private final ConfigurationHelper<MessageConfig> messagesHelper;

    public ConfigManager(File pluginFolder) {
        configHelper = createHelper(MainConfig.class, new File(pluginFolder, "config.yaml"));
        messagesHelper = createHelper(MessageConfig.class, new File(pluginFolder, "messages.yaml"), new ComponentSerializer());
        reload();
    }

    private static <T> ConfigurationHelper<T> createHelper(Class<T> configClass, File file, ValueSerialiser<?>... serialisers) {
        SnakeYamlOptions yamlOptions = new SnakeYamlOptions.Builder().commentMode(CommentMode.fullComments()).build();
        ConfigurationOptions.Builder optionBuilder = new ConfigurationOptions.Builder();
        if (serialisers != null && serialisers.length > 0) optionBuilder.addSerialisers(serialisers);
        ConfigurationFactory<T> configFactory = SnakeYamlConfigurationFactory.create(configClass, optionBuilder.build(), yamlOptions);
        return new ConfigurationHelper<>(file.getParentFile().toPath(), file.getName(), configFactory);
    }

    public void reload() {
        try {
            config = configHelper.reloadConfigData();
            messages = messagesHelper.reloadConfigData();
        } catch (IOException e) {
            logger.severe("Couldn't open config file!");
            e.printStackTrace();
        } catch (ConfigFormatSyntaxException e) {
            logger.severe("Invalid config syntax!");
            e.printStackTrace();
        } catch (InvalidConfigException e) {
            logger.severe("Invalid config value!");
            e.printStackTrace();
        }
    }

    public MainConfig getConfig() {
        return config;
    }

    public MessageConfig getMessages() {
        return messages;
    }
}
