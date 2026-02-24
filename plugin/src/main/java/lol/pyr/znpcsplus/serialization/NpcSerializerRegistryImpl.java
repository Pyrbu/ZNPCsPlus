package lol.pyr.znpcsplus.serialization;

import java.util.HashMap;
import java.util.Map;
import lol.pyr.znpcsplus.api.serialization.NpcSerializer;
import lol.pyr.znpcsplus.api.serialization.NpcSerializerRegistry;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;

public class NpcSerializerRegistryImpl implements NpcSerializerRegistry {
  private final Map<Class<?>, NpcSerializer<?>> serializerMap = new HashMap<>();

  public NpcSerializerRegistryImpl(
      PacketFactory packetFactory,
      ConfigManager configManager,
      ActionRegistryImpl actionRegistry,
      NpcTypeRegistryImpl typeRegistry,
      EntityPropertyRegistryImpl propertyRegistry,
      LegacyComponentSerializer textSerializer) {
    registerSerializer(
        YamlConfiguration.class,
        new YamlSerializer(
            packetFactory,
            configManager,
            actionRegistry,
            typeRegistry,
            propertyRegistry,
            textSerializer));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> NpcSerializer<T> getSerializer(Class<T> clazz) {
    return (NpcSerializer<T>) serializerMap.get(clazz);
  }

  @Override
  public <T> void registerSerializer(Class<T> clazz, NpcSerializer<T> serializer) {
    serializerMap.put(clazz, serializer);
  }
}
