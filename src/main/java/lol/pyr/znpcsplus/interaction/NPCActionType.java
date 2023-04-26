package lol.pyr.znpcsplus.interaction;

import lol.pyr.znpcsplus.interaction.types.*;

public enum NPCActionType implements NPCActionDeserializer {
    CONSOLE_CMD(ConsoleCommandAction::new),
    MESSAGE(MessageAction::new),
    PLAYER_CMD(PlayerCommandAction::new),
    SERVER(SwitchServerAction::new);

    private final NPCActionDeserializer deserializer;

    NPCActionType(NPCActionDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public NPCAction deserialize(long delay, String str) {
        return deserializer.deserialize(delay, str);
    }
}
