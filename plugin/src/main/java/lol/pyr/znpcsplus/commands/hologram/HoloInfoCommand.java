package lol.pyr.znpcsplus.commands.hologram;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.hologram.HologramLine;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class HoloInfoCommand implements CommandHandler {
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " holo info <id>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        HologramImpl hologram = entry.getNpc().getHologram();
        Component component = Component.text("NPC Hologram Info of ID " + entry.getId() + ":", NamedTextColor.GREEN).appendNewline();
        for (HologramLine line : hologram.getLines()) component = component.append(line.getText()).appendNewline();
        context.send(component);
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(NpcRegistryImpl.get().modifiableIds());
        return Collections.emptyList();
    }
}
