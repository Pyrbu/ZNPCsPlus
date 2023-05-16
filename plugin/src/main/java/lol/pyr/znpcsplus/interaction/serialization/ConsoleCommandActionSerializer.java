package lol.pyr.znpcsplus.interaction.serialization;

import lol.pyr.znpcsplus.interaction.types.ConsoleCommandAction;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.StringSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ConsoleCommandActionSerializer implements StringSerializer<ConsoleCommandAction> {
    private final TaskScheduler scheduler;

    public ConsoleCommandActionSerializer(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public String serialize(ConsoleCommandAction obj) {
        return Base64.getEncoder().encodeToString(obj.getCommand().getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown();
    }

    @Override
    public ConsoleCommandAction deserialize(String str) {
        String[] split = str.split(";");
        return new ConsoleCommandAction(scheduler, new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8), Long.parseLong(split[1]));
    }
}
