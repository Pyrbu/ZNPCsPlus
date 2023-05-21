package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ComponentPropertySerializer implements PropertySerializer<Component> {
    @Override
    public String serialize(Component property) {
        return Base64.getEncoder().encodeToString(MiniMessage.miniMessage().serialize(property).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Component deserialize(String property) {
        return MiniMessage.miniMessage().deserialize(new String(Base64.getDecoder().decode(property), StandardCharsets.UTF_8));
    }

    @Override
    public Class<Component> getTypeClass() {
        return Component.class;
    }
}
