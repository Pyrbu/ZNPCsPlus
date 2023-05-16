package lol.pyr.znpcsplus.interaction.serialization;

import lol.pyr.znpcsplus.interaction.types.MessageAction;
import lol.pyr.znpcsplus.util.StringSerializer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MessageActionSerializer implements StringSerializer<MessageAction> {
    private final BukkitAudiences adventure;

    public MessageActionSerializer(BukkitAudiences adventure) {
        this.adventure = adventure;
    }

    @Override
    public String serialize(MessageAction obj) {
        return Base64.getEncoder().encodeToString(MiniMessage.miniMessage().serialize(obj.getMessage()).getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown();
    }

    @Override
    public MessageAction deserialize(String str) {
        String[] split = str.split(";");
        return new MessageAction(adventure, MiniMessage.miniMessage().deserialize(new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8)), Long.parseLong(split[1]));
    }
}
