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

public class HoloDeleteCommand implements CommandHandler {
    @Override
    public void run(CommandContext commandContext) throws CommandExecutionException {
        if (commandContext.argSize() < 2) {
            ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(Component.text("Usage: /npc holo delete <npc_id> <line>", NamedTextColor.RED));
            return;
        }
        String id = commandContext.popString();
        NpcEntryImpl npcEntry = NpcRegistryImpl.get().get(id);
        if (npcEntry == null) {
            ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(Component.text("NPC not found!", NamedTextColor.RED));
            return;
        }
        int line;
        try {
            line = Integer.parseInt(commandContext.popString());
        } catch (NumberFormatException e) {
            ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(Component.text("Invalid line number!", NamedTextColor.RED));
            return;
        }
        if (line < 0 || line >= npcEntry.getNpc().getHologram().getLines().size()) {
            ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(Component.text("Invalid line number!", NamedTextColor.RED));
            return;
        }
        npcEntry.getNpc().getHologram().removeLine(line);
        ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(Component.text("NPC line removed!", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) {
            return context.suggestCollection(NpcRegistryImpl.get().ids().stream().filter(s -> !s.startsWith(ZNpcsPlus.DEBUG_NPC_PREFIX)).collect(Collectors.toList()));
        }
        if (context.argSize() == 2) {
            int lines = NpcRegistryImpl.get().get(context.popString()).getNpc().getHologram().getLines().size();
            return context.suggestStream(Stream.iterate(0, n -> n + 1).limit(lines).map(String::valueOf));
        }
        return CommandHandler.super.suggest(context);
    }
}
