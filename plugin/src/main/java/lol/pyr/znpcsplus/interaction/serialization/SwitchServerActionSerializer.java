package lol.pyr.znpcsplus.interaction.serialization;

import lol.pyr.znpcsplus.interaction.types.SwitchServerAction;
import lol.pyr.znpcsplus.util.BungeeUtil;
import lol.pyr.znpcsplus.util.StringSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SwitchServerActionSerializer implements StringSerializer<SwitchServerAction> {
    private final BungeeUtil bungeeUtil;

    public SwitchServerActionSerializer(BungeeUtil bungeeUtil) {
        this.bungeeUtil = bungeeUtil;
    }

    @Override
    public String serialize(SwitchServerAction obj) {
        return Base64.getEncoder().encodeToString(obj.getServer().getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown();
    }

    @Override
    public SwitchServerAction deserialize(String str) {
        String[] split = str.split(";");
        return new SwitchServerAction(bungeeUtil, new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8), Long.parseLong(split[1]));
    }
}
