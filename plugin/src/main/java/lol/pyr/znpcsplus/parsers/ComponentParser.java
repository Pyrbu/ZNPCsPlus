package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Deque;

public class ComponentParser extends ParserType<Component> {
    private final LegacyComponentSerializer textSerializer;

    public ComponentParser(Message<CommandContext> message, LegacyComponentSerializer textSerializer) {
        super(message);
        this.textSerializer = textSerializer;
    }

    @Override
    public Component parse(Deque<String> deque) throws CommandExecutionException {
        String line = String.join(" ", deque);
        Component component = line.contains("§") ? Component.text(line) : MiniMessage.miniMessage().deserialize(line);
        return textSerializer.deserialize(textSerializer.serialize(component));
    }
}
