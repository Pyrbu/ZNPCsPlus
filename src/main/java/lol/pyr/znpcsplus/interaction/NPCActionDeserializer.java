package lol.pyr.znpcsplus.interaction;

@FunctionalInterface
interface NPCActionDeserializer {
    NPCAction deserialize(long delay, String argument);
}