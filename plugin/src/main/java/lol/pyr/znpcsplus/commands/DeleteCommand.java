package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class DeleteCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;
    private final BukkitAudiences adventure;

    public DeleteCommand(NpcRegistryImpl npcRegistry, BukkitAudiences adventure) {
        this.npcRegistry = npcRegistry;
        this.adventure = adventure;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " delete <id>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        npcRegistry.delete(entry.getId());
        adventure.sender(context.getSender()).sendMessage(Component.text("Deleted NPC with ID: " + entry.getId(), NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        return Collections.emptyList();
    }
}
