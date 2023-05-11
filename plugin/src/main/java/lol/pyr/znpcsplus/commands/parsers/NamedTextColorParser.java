package lol.pyr.znpcsplus.commands.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Deque;

public class NamedTextColorParser extends ParserType<NamedTextColor> {
    public NamedTextColorParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public NamedTextColor parse(Deque<String> deque) throws CommandExecutionException {
        NamedTextColor color = NamedTextColor.NAMES.value(deque.pop());
        if (color == null) throw new CommandExecutionException();
        return color;
    }
}
