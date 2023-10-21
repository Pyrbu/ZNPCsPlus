package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class ChangeIdCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public ChangeIdCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " changeid <old> <new>");
        NpcEntryImpl old = context.parse(NpcEntryImpl.class);
        String newId = context.popString();
        if (npcRegistry.getById(newId) != null) context.halt(Component.text("There is already an npc with the new id you have provided", NamedTextColor.RED));
        npcRegistry.switchIds(old.getId(), newId);
        context.send(Component.text("Npc's id changed to " + newId.toLowerCase(), NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        return Collections.emptyList();
    }
}
