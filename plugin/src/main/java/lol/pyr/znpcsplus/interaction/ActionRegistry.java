package lol.pyr.znpcsplus.interaction;

import lol.pyr.znpcsplus.interaction.consolecommand.ConsoleCommandActionType;
import lol.pyr.znpcsplus.interaction.message.MessageActionType;
import lol.pyr.znpcsplus.interaction.playercommand.PlayerCommandActionType;
import lol.pyr.znpcsplus.interaction.switchserver.SwitchServerActionType;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.BungeeConnector;
import lol.pyr.znpcsplus.util.StringSerializer;
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

    public void registerTypes(NpcRegistryImpl npcRegistry, TaskScheduler taskScheduler, BukkitAudiences adventure, BungeeConnector bungeeConnector, LegacyComponentSerializer textSerializer) {
        register(new ConsoleCommandActionType(taskScheduler, npcRegistry));
        register(new PlayerCommandActionType(taskScheduler, npcRegistry));
        register(new SwitchServerActionType(bungeeConnector, npcRegistry));
        register(new MessageActionType(adventure, textSerializer, npcRegistry));
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
            StringSerializer<T> serializer = (StringSerializer<T>) serializerMap.get(clazz);
            if (serializer == null) return null;
            return serializer.deserialize(String.join(";", Arrays.copyOfRange(split, 1, split.length)));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends InteractionAction> String serialize(T action) {
        StringSerializer<T> serializer = (StringSerializer<T>) serializerMap.get(action.getClass());
        if (serializer == null) return null;
        return action.getClass().getName() + ";" + serializer.serialize(action);
    }
}
