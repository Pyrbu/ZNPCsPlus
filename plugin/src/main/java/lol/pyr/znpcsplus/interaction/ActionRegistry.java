package lol.pyr.znpcsplus.interaction;

import lol.pyr.znpcsplus.interaction.types.*;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.BungeeUtil;
import lol.pyr.znpcsplus.util.StringSerializer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActionRegistry {
    private final Map<Class<?>, StringSerializer<?>> serializerMap = new HashMap<>();

    public ActionRegistry(TaskScheduler taskScheduler, BukkitAudiences adventure, BungeeUtil bungeeUtil) {
        register(ConsoleCommandAction.class, new ConsoleCommandActionSerializer(taskScheduler));
        register(PlayerCommandAction.class, new PlayerCommandActionSerializer(taskScheduler));
        register(SwitchServerAction.class, new SwitchServerActionSerializer(bungeeUtil));
        register(MessageAction.class, new MessageActionSerializer(adventure));
    }

    public <T extends InteractionAction> void register(Class<T> clazz, StringSerializer<T> serializer) {
        serializerMap.put(clazz, serializer);
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
