package lol.pyr.znpcsplus.storage.yaml;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.hologram.HologramLine;
import lol.pyr.znpcsplus.interaction.NpcAction;
import lol.pyr.znpcsplus.interaction.NpcActionType;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.util.ZLocation;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class YamlStorage implements NpcStorage {
    private final File npcsFolder;

    public YamlStorage() {
        npcsFolder = new File(ZNpcsPlus.PLUGIN_FOLDER, "npcs");
        if (!npcsFolder.exists()) npcsFolder.mkdirs();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Collection<NpcEntryImpl> loadNpcs() {
        File[] files = npcsFolder.listFiles();
        if (files == null || files.length == 0) return Collections.emptyList();
        List<NpcEntryImpl> npcs = new ArrayList<>();
        for (File file : files) if (file.isFile() && file.getName().toLowerCase().endsWith(".yml")) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            NpcImpl npc = new NpcImpl(config.getString("world"), NpcTypeImpl.byName(config.getString("type")),
                    deserializeLocation(config.getConfigurationSection("location")));

            ConfigurationSection properties = config.getConfigurationSection("properties");
            for (String key : properties.getKeys(false)) {
                EntityPropertyImpl<?> property = EntityPropertyImpl.getByName(key);
                npc.UNSAFE_setProperty(property, property.deserialize(properties.getString(key)));
            }

            for (String line : config.getStringList("hologram")) {
                npc.getHologram().addLine(MiniMessage.miniMessage().deserialize(line));
            }

            int amt = config.getInt("action-amount");
            for (int i = 1; i <= amt; i++) {
                String key = "actions." + i;
                npc.addAction(NpcActionType.valueOf(config.getString(key + ".type"))
                        .deserialize(config.getInt(key + ".cooldown"), config.getString(key + ".argument")));
            }

            NpcEntryImpl entry = new NpcEntryImpl(config.getString("id"), npc);
            entry.setProcessed(config.getBoolean("is-processed"));
            entry.setAllowCommandModification(config.getBoolean("allow-commands"));
            entry.setSave(true);

            npcs.add(entry);
        }
        return npcs;
    }

    @Override
    public void saveNpcs(Collection<NpcEntryImpl> npcs) {
        File[] files = npcsFolder.listFiles();
        if (files != null && files.length != 0) for (File file : files) file.delete();
        for (NpcEntryImpl entry : npcs) try {
            YamlConfiguration config = new YamlConfiguration();
            config.set("id", entry.getId());
            config.set("is-processed", entry.isProcessed());
            config.set("allow-commands", entry.isAllowCommandModification());

            NpcImpl npc = entry.getNpc();
            config.set("world", npc.getWorldName());
            config.set("location", serializeLocation(npc.getLocation()));
            config.set("type", npc.getType().getName());

            for (EntityPropertyImpl<?> property : npc.getAppliedProperties()) {
                config.set("properties." + property.getName(), property.serialize(npc));
            }

            List<String> lines = new ArrayList<>();
            for (HologramLine line : npc.getHologram().getLines()) {
                lines.add(MiniMessage.miniMessage().serialize(line.getText()));
            }
            config.set("hologram", lines);

            int i = 0;
            for (NpcAction action : npc.getActions()) { i++;
                String key = "actions." + i;
                config.set(key + ".type", action.getType().name());
                config.set(key + ".cooldown", action.getCooldown());
                config.set(key + ".argument", action.getArgument());
            }
            config.set("action-amount", i);

            config.save(new File(npcsFolder, entry.getId() + ".yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ZLocation deserializeLocation(ConfigurationSection section) {
        return new ZLocation(
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
    }

    public YamlConfiguration serializeLocation(ZLocation location) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("x", location.getX());
        config.set("y", location.getY());
        config.set("z", location.getZ());
        config.set("yaw", location.getYaw());
        config.set("pitch", location.getPitch());
        return config;
    }
}
