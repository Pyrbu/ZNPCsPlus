package lol.pyr.znpcsplus.api.serialization;

import lol.pyr.znpcsplus.api.npc.NpcEntry;

public interface NpcSerializer<T> {
  /**
   * Serialize an npc into the type of this serializer
   *
   * @param entry The npc entry
   * @return The serialized class
   */
  T serialize(NpcEntry entry);

  /**
   * Deserialize an npc from a serialized class Note: This npc will not be registered, you need to
   * also register it using the NpcRegistry#register(NpcEntry) method
   *
   * @param model The serialized class
   * @return The deserialized NpcEntry
   */
  NpcEntry deserialize(T model);
}
