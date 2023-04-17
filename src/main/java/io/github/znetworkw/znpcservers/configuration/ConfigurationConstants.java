package io.github.znetworkw.znpcservers.configuration;

import io.github.znetworkw.znpcservers.npc.NPCModel;
import io.github.znetworkw.znpcservers.npc.conversation.Conversation;

import java.util.List;

public final class ConfigurationConstants {
    public static final String SPACE_SYMBOL = Configuration.CONFIGURATION.getValue(ConfigurationValue.REPLACE_SYMBOL);

    public static final int VIEW_DISTANCE = Configuration.CONFIGURATION.<Integer>getValue(ConfigurationValue.VIEW_DISTANCE).intValue();

    public static final int SAVE_DELAY = Configuration.CONFIGURATION.<Integer>getValue(ConfigurationValue.SAVE_NPCS_DELAY_SECONDS).intValue();

    public static final boolean RGB_ANIMATION = Configuration.CONFIGURATION.<Boolean>getValue(ConfigurationValue.ANIMATION_RGB).booleanValue();

    public static final List<NPCModel> NPC_LIST = Configuration.DATA.getValue(ConfigurationValue.NPC_LIST);

    public static final List<Conversation> NPC_CONVERSATIONS = Configuration.CONVERSATIONS.getValue(ConfigurationValue.CONVERSATION_LIST);

    static {
        NPC_LIST.stream()
                .map(io.github.znetworkw.znpcservers.npc.NPC::new)
                .forEach(io.github.znetworkw.znpcservers.npc.task.NPCLoadTask::new);
    }
}
