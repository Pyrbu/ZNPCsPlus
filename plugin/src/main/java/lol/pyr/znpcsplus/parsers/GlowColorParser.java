package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.util.GlowColor;
import java.util.Deque;

public class GlowColorParser extends ParserType<GlowColor> {
    public GlowColorParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public GlowColor parse(Deque<String> deque) throws CommandExecutionException {
        GlowColor color = GlowColor.NAMES.value(deque.pop());
        if (color == null) throw new CommandExecutionException();
        return color;
    }
}