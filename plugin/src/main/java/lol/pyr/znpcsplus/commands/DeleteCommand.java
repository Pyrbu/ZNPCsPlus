package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class DeleteCommand implements CommandHandler {
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        if (context.argSize() != 1) {
            ZNpcsPlus.ADVENTURE.sender(context.getSender()).sendMessage(Component.text("Usage: /npc delete <npc_id>", NamedTextColor.RED));
            return;
        }
        String id = context.popString();
        NpcEntryImpl npcEntry = NpcRegistryImpl.get().get(id);
        if (npcEntry == null) {
            ZNpcsPlus.ADVENTURE.sender(context.getSender()).sendMessage(Component.text("NPC not found!", NamedTextColor.RED));
            return;
        }
        NpcRegistryImpl.get().delete(id);
        ZNpcsPlus.ADVENTURE.sender(context.getSender()).sendMessage(Component.text("Deleted NPC with ID: " + id, NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(NpcRegistryImpl.get().modifiableIds());
        return Collections.emptyList();
    }
}
