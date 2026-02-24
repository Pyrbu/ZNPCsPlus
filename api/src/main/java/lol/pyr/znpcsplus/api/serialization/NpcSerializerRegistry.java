package lol.pyr.znpcsplus.api.serialization;

public interface NpcSerializerRegistry {
  /**
   * Get an NpcSerializer that serializes npcs into the provided class
   *
   * @param clazz The class to serialize into
   * @return The npc serializer instance
   * @param <T> The type of the class that the serializer serializes into
   */
  <T> NpcSerializer<T> getSerializer(Class<T> clazz);

  /**
   * Register an NpcSerializer to be used by other plugins
   *
   * @param clazz The class that the serializer serializes into
   * @param serializer The serializer itself
   * @param <T> The type of the class that the serializer serializes into
   */
  <T> void registerSerializer(Class<T> clazz, NpcSerializer<T> serializer);
}
