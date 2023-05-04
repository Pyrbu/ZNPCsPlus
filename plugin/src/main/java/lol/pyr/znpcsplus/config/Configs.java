package lol.pyr.znpcsplus.config;

import lol.pyr.znpcsplus.ZNpcsPlus;
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

public class Configs {
    private volatile static MainConfig config;
    private static ConfigurationHelper<MainConfig> configHelper;

    private volatile static MessageConfig messages;
    private static ConfigurationHelper<MessageConfig> messagesHelper;

    private static <T> ConfigurationHelper<T> createHelper(Class<T> configClass, File file, ValueSerialiser<?>... serialisers) {
        SnakeYamlOptions yamlOptions = new SnakeYamlOptions.Builder().commentMode(CommentMode.fullComments()).build();
        ConfigurationOptions.Builder optionBuilder = new ConfigurationOptions.Builder();
        if (serialisers != null && serialisers.length > 0) optionBuilder.addSerialisers(serialisers);
        ConfigurationFactory<T> configFactory = SnakeYamlConfigurationFactory.create(configClass, optionBuilder.build(), yamlOptions);
        return new ConfigurationHelper<>(file.getParentFile().toPath(), file.getName(), configFactory);
    }

    public static void init(File pluginFolder) {
        configHelper = createHelper(MainConfig.class, new File(pluginFolder, "config.yaml"));
        messagesHelper = createHelper(MessageConfig.class, new File(pluginFolder, "messages.yaml"), new ComponentSerializer());
        load();
    }

    public static void load() {
        try {
            config = configHelper.reloadConfigData();
            messages = messagesHelper.reloadConfigData();
        } catch (IOException e) {
            ZNpcsPlus.LOGGER.severe("Couldn't open config file!");
            e.printStackTrace();
        } catch (ConfigFormatSyntaxException e) {
            ZNpcsPlus.LOGGER.severe("Invalid config syntax!");
            e.printStackTrace();
        } catch (InvalidConfigException e) {
            ZNpcsPlus.LOGGER.severe("Invalid config value!");
            e.printStackTrace();
        }
    }

    public static MainConfig config() {
        return config;
    }

    public static MessageConfig messages() {
        return messages;
    }
}
