package lol.pyr.znpcsplus.interaction;

import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.interaction.consolecommand.ConsoleCommandActionType;
import lol.pyr.znpcsplus.interaction.message.MessageActionType;
import lol.pyr.znpcsplus.interaction.playerchat.PlayerChatActionType;
import lol.pyr.znpcsplus.interaction.playercommand.PlayerCommandActionType;
import lol.pyr.znpcsplus.interaction.switchserver.SwitchServerActionType;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.BungeeConnector;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionRegistry {
    private final Map<Class<?>, InteractionActionType<?>> serializerMap = new HashMap<>();

    public ActionRegistry() {
    }

    public void registerTypes(TaskScheduler taskScheduler, BukkitAudiences adventure, LegacyComponentSerializer textSerializer, BungeeConnector bungeeConnector) {
        register(new ConsoleCommandActionType(taskScheduler));
        register(new PlayerCommandActionType(taskScheduler));
        register(new SwitchServerActionType(bungeeConnector));
        register(new MessageActionType(adventure, textSerializer));
        register(new PlayerChatActionType(taskScheduler));
    }

    public void register(InteractionActionType<?> type) {
        serializerMap.put(type.getActionClass(), type);
    }

    public void unregister(Class<? extends InteractionAction> clazz) {
        serializerMap.remove(clazz);
    }

    public List<InteractionCommandHandler> getCommands() {
        return serializerMap.values().stream()
                .filter(type -> type instanceof InteractionCommandHandler)
                .map(type -> (InteractionCommandHandler) type)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T extends InteractionAction> T deserialize(String str) {
        try {
            String[] split = str.split(";");
            Class<?> clazz = Class.forName(split[0]);
            InteractionActionType<T> serializer = (InteractionActionType<T>) serializerMap.get(clazz);
            if (serializer == null) return null;
            return serializer.deserialize(String.join(";", Arrays.copyOfRange(split, 1, split.length)));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends InteractionAction> String serialize(T action) {
        InteractionActionType<T> serializer = (InteractionActionType<T>) serializerMap.get(action.getClass());
        if (serializer == null) return null;
        return action.getClass().getName() + ";" + serializer.serialize(action);
    }
}
