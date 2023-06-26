package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import org.bukkit.Color;

import java.util.Deque;

public class ColorParser extends ParserType<Color> {
    public ColorParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public Color parse(Deque<String> deque) throws CommandExecutionException {
        String color = deque.pop();
        if (color.startsWith("0x")) color = color.substring(2);
        if (color.startsWith("&")) color = color.substring(1);
        if (color.startsWith("#")) color = color.substring(1);
        try {
            return Color.fromRGB(Integer.parseInt(color, 16));
        } catch (IllegalArgumentException exception) {
            throw new CommandExecutionException();
        }
    }
}
