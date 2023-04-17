package io.github.znetworkw.znpcservers.configuration;

import com.google.common.collect.ImmutableSet;
import io.github.znetworkw.znpcservers.npc.NPCModel;
import io.github.znetworkw.znpcservers.npc.NamingType;
import io.github.znetworkw.znpcservers.npc.conversation.Conversation;
import io.github.znetworkw.znpcservers.utility.GuavaCollectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ConfigurationValue {
    NPC_LIST("data", new ArrayList(), NPCModel.class),
    VIEW_DISTANCE("config", Integer.valueOf(32), Integer.class),
    REPLACE_SYMBOL("config", "-", String.class),
    SAVE_NPCS_DELAY_SECONDS("config", Integer.valueOf(600), Integer.class),
    MAX_PATH_LOCATIONS("config", Integer.valueOf(500), Integer.class),
    NAMING_METHOD("config", NamingType.DEFAULT, NamingType.class),
    DEBUG_ENABLED("config", Boolean.TRUE, Boolean.class),
    LINE_SPACING("config", Double.valueOf(0.3D), Double.class),
    ANIMATION_RGB("config", Boolean.FALSE, Boolean.class),
    NO_PERMISSION("messages", "&cYou do not have permission to execute this command.", String.class),
    SUCCESS("messages", "&aDone...", String.class),
    INCORRECT_USAGE("messages", "&cIncorrect use of command.", String.class),
    COMMAND_NOT_FOUND("messages", "&cThis command was not found.", String.class),
    COMMAND_ERROR("messages", "&cThere was an error executing the command, see the console for more information.", String.class),
    INVALID_NUMBER("messages", "&cHey!, The inserted number/id does not look like a number..", String.class),
    NPC_NOT_FOUND("messages", "&cHey!, I couldnt find a npc with this id.", String.class),
    TOO_FEW_ARGUMENTS("messages", "&cToo few arguments.", String.class),
    PATH_START("messages", "&aDone, now walk where you want the npc to, when u finish type /znpcs path exit.", String.class),
    EXIT_PATH("messages", "&cYou have exited the waypoint creation.", String.class),
    PATH_FOUND("messages", "&cThere is already a path with this name.", String.class),
    NPC_FOUND("messages", "&cThere is already a npc with this id.", String.class),
    NO_PATH_FOUND("messages", "&cNo path found.", String.class),
    NO_SKIN_FOUND("messages", "&cSkin not found.", String.class),
    NO_NPC_FOUND("messages", "&cNo npc found.", String.class),
    NO_ACTION_FOUND("messages", "&cNo action found.", String.class),
    METHOD_NOT_FOUND("messages", "&cNo method found.", String.class),
    INVALID_NAME_LENGTH("messages", "&cThe name is too short or long, it must be in the range of (3 to 16) characters.", String.class),
    UNSUPPORTED_ENTITY("messages", "&cEntity type not available for your current version.", String.class),
    PATH_SET_INCORRECT_USAGE("messages", "&eUsage: &aset <npc_id> <path_name>", String.class),
    ACTION_ADD_INCORRECT_USAGE("messages", "&eUsage: &a<SERVER:CMD:MESSAGE:CONSOLE> <actionValue>", String.class),
    ACTION_DELAY_INCORRECT_USAGE("messages", "&eUsage: &a<action_id> <delay>", String.class),
    CONVERSATION_SET_INCORRECT_USAGE("messages", "&cUsage: <npc_id> <conversation_name> <RADIUS:CLICK>", String.class),
    NO_CONVERSATION_FOUND("messages", "&cNo conversation found.", String.class),
    CONVERSATION_FOUND("messages", "&cThere is already a conversation with this name.", String.class),
    INVALID_SIZE("messages", "&cThe position cannot exceed the limit.", String.class),
    FETCHING_SKIN("messages", "&aFetching skin for name: &f%s&a, wait...", String.class),
    CANT_GET_SKIN("messages", "&ccan't fetch skin with name: %s.", String.class),
    GET_SKIN("messages", "&aSkin fetched.", String.class),
    CONVERSATION_LIST("conversations", new ArrayList(), Conversation.class);

    public static final Map<String, ImmutableSet<ConfigurationValue>> VALUES_BY_NAME;

    static {
        VALUES_BY_NAME = Arrays.stream(values()).collect(Collectors.groupingBy(ConfigurationValue::getConfigName, GuavaCollectors.toImmutableSet()));
    }

    private final String configName;
    private final Object value;
    private final Class<?> primitiveType;

    ConfigurationValue(String configName, Object value, Class<?> primitiveType) {
        this.configName = configName;
        this.value = value;
        this.primitiveType = primitiveType;
    }

    public String getConfigName() {
        return this.configName;
    }

    public Object getValue() {
        return this.value;
    }

    public Class<?> getPrimitiveType() {
        return this.primitiveType;
    }
}
