package lol.pyr.znpcsplus.interaction;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public interface InteractionCommandHandler extends CommandHandler {
    String getSubcommandName();

    InteractionActionImpl parse(CommandContext context) throws CommandExecutionException;
    void appendUsage(CommandContext context);

    @Override
    default void run(CommandContext context) throws CommandExecutionException {
        appendUsage(context);
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        npc.addAction(parse(context));
        context.send(Component.text("Added action to npc", NamedTextColor.GREEN));
    }
}
