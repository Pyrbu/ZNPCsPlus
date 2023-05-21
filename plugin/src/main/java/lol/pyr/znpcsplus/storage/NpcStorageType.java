package lol.pyr.znpcsplus.storage;

import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.interaction.ActionRegistry;
import lol.pyr.znpcsplus.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.storage.yaml.YamlStorage;

import java.io.File;

public enum NpcStorageType {
    YAML {
        @Override
        public NpcStorage create(ConfigManager configManager, ZNpcsPlus plugin, PacketFactory packetFactory, ActionRegistry actionRegistry, NpcTypeRegistry typeRegistry) {
            return new YamlStorage(packetFactory, configManager, actionRegistry, typeRegistry, new File(plugin.getDataFolder(), "data"));
        }
    };

    public abstract NpcStorage create(ConfigManager configManager, ZNpcsPlus plugin, PacketFactory packetFactory, ActionRegistry actionRegistry, NpcTypeRegistry typeRegistry);
}
