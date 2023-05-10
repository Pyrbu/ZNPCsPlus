package lol.pyr.znpcsplus.commands.hologram;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HoloAddCommand implements CommandHandler {
    @Override
    public void run(CommandContext commandContext) throws CommandExecutionException {
        if (commandContext.argSize() < 2) {
            ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(Component.text("Usage: /npc holo add <npc_id> <text>", NamedTextColor.RED));
            return;
        }
        String id = commandContext.popString();
        NpcEntryImpl npcEntry = NpcRegistryImpl.get().get(id);
        if (npcEntry == null) {
            ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(Component.text("NPC not found!", NamedTextColor.RED));
            return;
        }
        npcEntry.getNpc().getHologram().addLine(Component.text(commandContext.dumpAllArgs()));
        ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(Component.text("NPC line added!", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) {
            return context.suggestCollection(NpcRegistryImpl.get().ids().stream().filter(s -> !s.startsWith(ZNpcsPlus.DEBUG_NPC_PREFIX)).collect(Collectors.toList()));
        }
        if (context.argSize() == 2) {
            return context.suggestStream(Stream.of("<text>"));
        }
        return CommandHandler.super.suggest(context);
    }
}
