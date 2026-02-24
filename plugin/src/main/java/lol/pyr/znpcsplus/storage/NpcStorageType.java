package lol.pyr.znpcsplus.storage;

import java.io.File;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.serialization.NpcSerializerRegistryImpl;
import lol.pyr.znpcsplus.storage.mysql.MySQLStorage;
import lol.pyr.znpcsplus.storage.sqlite.SQLiteStorage;
import lol.pyr.znpcsplus.storage.yaml.YamlStorage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public enum NpcStorageType {
  YAML {
    @Override
    public NpcStorage create(
        ConfigManager configManager,
        ZNpcsPlus plugin,
        PacketFactory packetFactory,
        ActionRegistryImpl actionRegistry,
        NpcTypeRegistryImpl typeRegistry,
        EntityPropertyRegistryImpl propertyRegistry,
        LegacyComponentSerializer textSerializer,
        NpcSerializerRegistryImpl serializerRegistry) {
      return new YamlStorage(serializerRegistry, new File(plugin.getDataFolder(), "data"));
    }
  },
  SQLITE {
    @Override
    public NpcStorage create(
        ConfigManager configManager,
        ZNpcsPlus plugin,
        PacketFactory packetFactory,
        ActionRegistryImpl actionRegistry,
        NpcTypeRegistryImpl typeRegistry,
        EntityPropertyRegistryImpl propertyRegistry,
        LegacyComponentSerializer textSerializer,
        NpcSerializerRegistryImpl serializerRegistry) {
      try {
        return new SQLiteStorage(
            packetFactory,
            configManager,
            actionRegistry,
            typeRegistry,
            propertyRegistry,
            textSerializer,
            new File(plugin.getDataFolder(), "znpcsplus.sqlite"));
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
  },
  MYSQL {
    @Override
    public NpcStorage create(
        ConfigManager configManager,
        ZNpcsPlus plugin,
        PacketFactory packetFactory,
        ActionRegistryImpl actionRegistry,
        NpcTypeRegistryImpl typeRegistry,
        EntityPropertyRegistryImpl propertyRegistry,
        LegacyComponentSerializer textSerializer,
        NpcSerializerRegistryImpl serializerRegistry) {
      try {
        return new MySQLStorage(
            packetFactory,
            configManager,
            actionRegistry,
            typeRegistry,
            propertyRegistry,
            textSerializer);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
  };

  public abstract NpcStorage create(
      ConfigManager configManager,
      ZNpcsPlus plugin,
      PacketFactory packetFactory,
      ActionRegistryImpl actionRegistry,
      NpcTypeRegistryImpl typeRegistry,
      EntityPropertyRegistryImpl propertyRegistry,
      LegacyComponentSerializer textSerializer,
      NpcSerializerRegistryImpl serializerRegistry);
}
