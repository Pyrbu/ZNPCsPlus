package lol.pyr.znpcsplus.commands.hologram;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

import java.util.Collections;
import java.util.List;

public class HoloOffsetCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public HoloOffsetCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " holo offset <id> <offset>");
        HologramImpl hologram = context.parse(NpcEntryImpl.class).getNpc().getHologram();
        double offset = context.parse(Double.class);
        hologram.setOffset(offset);
        context.send("NPC hologram offset set!");
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) {
            HologramImpl hologram = context.suggestionParse(0, NpcEntryImpl.class).getNpc().getHologram();
            return context.suggestLiteral(String.valueOf(hologram.getOffset()));
        }
        return Collections.emptyList();
    }
}
