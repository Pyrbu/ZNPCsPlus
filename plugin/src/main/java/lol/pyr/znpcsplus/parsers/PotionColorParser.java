package lol.pyr.znpcsplus.parsers;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.parse.ParserType;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.director.common.message.Message;
import lol.pyr.znpcsplus.util.PotionColor;

import java.util.Deque;

public class PotionColorParser extends ParserType<PotionColor> {
    public PotionColorParser(Message<CommandContext> message) {
        super(message);
    }

    @Override
    public PotionColor parse(Deque<String> deque) throws CommandExecutionException {
        return new PotionColor(deque.pop());
    }
}
