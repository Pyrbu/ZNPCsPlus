package io.github.znetworkw.znpcservers.configuration;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.znetworkw.znpcservers.utility.Utils;
import lol.pyr.znpcsplus.ZNPCsPlus;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

public class Configuration {
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final String name;
    private final Path path;
    private final Map<ConfigurationValue, Object> configurationValues;
    public static final Configuration CONFIGURATION = new Configuration("config");
    public static final Configuration MESSAGES = new Configuration("messages");
    public static final Configuration CONVERSATIONS = new Configuration("conversations");
    public static final Configuration DATA = new Configuration("data");
    public static final ImmutableList<Configuration> SAVE_CONFIGURATIONS = ImmutableList.of(CONVERSATIONS, DATA);

    protected Configuration(String name) {
        this(name, ZNPCsPlus.PLUGIN_FOLDER.toPath().resolve(name + ".json"));
    }

    private Configuration(String name, Path path) {
        if (!path.getFileName().toString().endsWith(".json")) {
            throw new IllegalStateException("invalid configuration format for: " + path.getFileName());
        } else {
            this.name = name;
            this.path = path;
            this.configurationValues = ConfigurationValue.VALUES_BY_NAME.get(name).stream().collect(Collectors.toMap((c) -> c, ConfigurationValue::getValue));
            this.onLoad();
        }
    }

    protected void onLoad() {
        synchronized(this.path) {
            try {
                Reader reader = Files.newBufferedReader(this.path, CHARSET);

                try {
                    JsonElement data = JsonParser.parseReader(reader);
                    if (data != null) {
                        for(ConfigurationValue configValue : this.configurationValues.keySet()) {
                            boolean single = this.configurationValues.size() == 1;
                            JsonElement jsonElement = single ? data : (data.isJsonObject() ? data.getAsJsonObject().get(configValue.name()) : null);
                            if (jsonElement != null && !jsonElement.isJsonNull()) {
                                if (!single && configValue.getPrimitiveType().isEnum()) {
                                    this.configurationValues.put(configValue, ZNPCsPlus.GSON.fromJson(jsonElement, configValue.getPrimitiveType()));
                                } else {
                                    throw new RuntimeException();
                                    // what is this fuckery??
                                    // this.configurationValues.put(configValue, ZNPCsPlus.GSON.fromJson(jsonElement, $Gson$Types.newParameterizedTypeWithOwner((Type)null, configValue.getValue().getClass(), new Type[]{configValue.getPrimitiveType()})));
                                }
                            }
                        }

                        reader.close();

                        return;
                    }

                    reader.close();
                } catch (Throwable var17) {
                    try {
                        reader.close();
                    } catch (Throwable var16) {
                        var17.addSuppressed(var16);
                    }

                    throw var17;
                }
            } catch (NoSuchFileException ignored) {
            } catch (IOException var19) {
                throw new IllegalStateException("Failed to read config: " + this.name);
            } finally {
                this.save();
            }

        }
    }

    public void save() {
        synchronized(this.path) {
            try {
                Writer writer = Files.newBufferedWriter(this.path, CHARSET);

                try {
                    ZNPCsPlus.GSON.toJson(this.configurationValues.size() == 1 ? this.configurationValues.values().iterator().next() : this.configurationValues, writer);
                    writer.close();
                } catch (Throwable var7) {
                    try {
                        writer.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }

                    throw var7;
                }
            } catch (IOException var8) {
                throw new IllegalStateException("Failed to save config: " + this.name);
            }

        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(ConfigurationValue configValue) {
        synchronized(this.path) {
            return (T)this.configurationValues.get(configValue);
        }
    }

    public void sendMessage(org.bukkit.command.CommandSender sender, ConfigurationValue configValue, Object... replaces) {
        sender.sendMessage(Utils.toColor(String.format(this.getValue(configValue), replaces)));
    }
}
