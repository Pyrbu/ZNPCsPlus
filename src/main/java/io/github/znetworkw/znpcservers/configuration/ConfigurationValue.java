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
    NPC_LIST("data", new ArrayList<>(), NPCModel.class),
    VIEW_DISTANCE("config", 32, Integer.class),
    REPLACE_SYMBOL("config", "-", String.class),
    SAVE_NPCS_DELAY_SECONDS("config", 600, Integer.class),
    MAX_PATH_LOCATIONS("config", 500, Integer.class),
    NAMING_METHOD("config", NamingType.DEFAULT, NamingType.class),
    DEBUG_ENABLED("config", true, Boolean.class),
    LINE_SPACING("config", 0.3D, Double.class),
    ANIMATION_RGB("config", false, Boolean.class),
    CHECK_FOR_UPDATES("config", true, Boolean.class),
    NO_PERMISSION("messages", "&cYou do not have permission to execute this command.", String.class),
    SUCCESS("messages", "&aSuccess!", String.class),
    INCORRECT_USAGE("messages", "&cThe arguments you specified are invalid. Type &f/znpcs help&c for examples.", String.class),
    COMMAND_NOT_FOUND("messages", "&cThe command you specified does not exist!", String.class),
    COMMAND_ERROR("messages", "&cAn error occurred when executing this command. See console for more information.", String.class),
    INVALID_NUMBER("messages", "&cThe ID you have specified is invalid. Please use positive integers only!", String.class),
    NPC_NOT_FOUND("messages", "&cNo NPCs could be found with this ID!", String.class),
    TOO_FEW_ARGUMENTS("messages", "&cThis command does not contain enough arguments. Type &f/znpcs help&c or view our documentation for a list/examples of existing arguments.", String.class),
    PATH_START("messages", "&aSuccess! Move to create a path for your NPC. When finished, type &f/znpcs path exit&c to exit path creation.", String.class),
    EXIT_PATH("messages", "&cYou have exited path creation.", String.class),
    PATH_FOUND("messages", "&cThere is already a path with this name.", String.class),
    NPC_FOUND("messages", "&cThere is already an NPC with this ID.", String.class),
    NO_PATH_FOUND("messages", "&cThe path you have specified does not exist.", String.class),
    NO_SKIN_FOUND("messages", "&cThe skin username/URL you have specified does not exist or is invalid.", String.class),
    NO_NPC_FOUND("messages", "&cThe NPC you have specified does not exist.", String.class),
    NO_ACTION_FOUND("messages", "&cThis action does not exist! Type &f/znpcs help&c or view our documentation for a list/examples of existing action types.", String.class),
    METHOD_NOT_FOUND("messages", "&cThis method does not exist! Type &f/znpcs help&c or view our documentation for a list/examples of existing methods.", String.class),
    INVALID_NAME_LENGTH("messages", "&cThe name you specified either too short or long. Please enter a positive integer of (3 to 16) characters.", String.class),
    UNSUPPORTED_ENTITY("messages", "&cThis entity type not available in your current server version.", String.class),
    PATH_SET_INCORRECT_USAGE("messages", "&eUsage: &aset <npc_id> <path_name>", String.class),
    ACTION_ADD_INCORRECT_USAGE("messages", "&eUsage: &a<SERVER/CMD/MESSAGE/CONSOLE> <actionValue>", String.class),
    ACTION_DELAY_INCORRECT_USAGE("messages", "&eUsage: &a<action_id> <delay>", String.class),
    CONVERSATION_SET_INCORRECT_USAGE("messages", "&cUsage: <npc_id> <conversation_name> <RADIUS/CLICK>", String.class),
    NO_CONVERSATION_FOUND("messages", "&cThe conversation you have specified does not exist!", String.class),
    CONVERSATION_FOUND("messages", "&cThere is already a conversation with this name.", String.class),
    INVALID_SIZE("messages", "&cThe position you have specified cannot exceed the limit.", String.class),
    FETCHING_SKIN("messages", "&aFetching skin for name: &f%s&a. Please wait...", String.class),
    CANT_GET_SKIN("messages", "&cCould not fetch skin for name: %s.", String.class),
    GET_SKIN("messages", "&aSkin successfully fetched!", String.class),
    CONVERSATION_LIST("conversations" /* Leave this lowercase or it will break */, new ArrayList<>(), Conversation.class);

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
