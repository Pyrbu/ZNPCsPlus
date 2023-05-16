package lol.pyr.znpcsplus.interaction.types;

import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.StringSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PlayerCommandActionSerializer implements StringSerializer<PlayerCommandAction> {
    private final TaskScheduler scheduler;

    public PlayerCommandActionSerializer(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public String serialize(PlayerCommandAction obj) {
        return Base64.getEncoder().encodeToString(obj.getCommand().getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown();
    }

    @Override
    public PlayerCommandAction deserialize(String str) {
        String[] split = str.split(";");
        return new PlayerCommandAction(scheduler, new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8), Long.parseLong(split[1]));
    }
}
