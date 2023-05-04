package lol.pyr.znpcsplus.interaction;

import lol.pyr.znpcsplus.interaction.types.*;

public enum NpcActionType implements NpcActionDeserializer {
    CONSOLE_CMD(ConsoleCommandAction::new),
    MESSAGE(MessageAction::new),
    PLAYER_CMD(PlayerCommandAction::new),
    SERVER(SwitchServerAction::new);

    private final NpcActionDeserializer deserializer;

    NpcActionType(NpcActionDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public NpcAction deserialize(long delay, String str) {
        return deserializer.deserialize(delay, str);
    }
}
