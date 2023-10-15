package lol.pyr.znpcsplus.storage;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.storage.sqlite.SQLiteStorage;
import lol.pyr.znpcsplus.storage.yaml.YamlStorage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.File;

public enum NpcStorageType {
    YAML {
        @Override
        public NpcStorage create(ConfigManager configManager, ZNpcsPlus plugin, PacketFactory packetFactory, ActionRegistry actionRegistry, NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer) {
            return new YamlStorage(packetFactory, configManager, actionRegistry, typeRegistry, propertyRegistry, textSerializer, new File(plugin.getDataFolder(), "data"));
        }
    },
    SQLITE {
        @Override
        public NpcStorage create(ConfigManager configManager, ZNpcsPlus plugin, PacketFactory packetFactory, ActionRegistry actionRegistry, NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer) {
            try {
                return new SQLiteStorage(packetFactory, configManager, actionRegistry, typeRegistry, propertyRegistry, textSerializer, new File(plugin.getDataFolder(), "znpcsplus.sqlite"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    };

    public abstract NpcStorage create(ConfigManager configManager, ZNpcsPlus plugin, PacketFactory packetFactory, ActionRegistry actionRegistry, NpcTypeRegistryImpl typeRegistry, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer);
}
