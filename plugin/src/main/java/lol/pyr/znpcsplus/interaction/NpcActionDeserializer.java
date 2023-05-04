package lol.pyr.znpcsplus.interaction;

@FunctionalInterface
interface NpcActionDeserializer {
    NpcAction deserialize(long delay, String argument);
}
