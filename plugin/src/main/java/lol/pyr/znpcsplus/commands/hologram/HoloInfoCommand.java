package lol.pyr.znpcsplus.commands.hologram;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class HoloInfoCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public HoloInfoCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " holo info <id>");
        NpcEntryImpl entry = context.parse(NpcEntryImpl.class);
        HologramImpl hologram = entry.getNpc().getHologram();
        Component component = Component.text("NPC Hologram Info of ID " + entry.getId() + ":", NamedTextColor.GREEN).appendNewline();
        for (int i = 0; i < hologram.getLines().size(); i++) {
            component = component.append(Component.text(i + ") ", NamedTextColor.GREEN))
                    .append(Component.text(hologram.getLine(i), NamedTextColor.WHITE))
                    .appendNewline();
        }
        context.send(component);
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        return Collections.emptyList();
    }
}
