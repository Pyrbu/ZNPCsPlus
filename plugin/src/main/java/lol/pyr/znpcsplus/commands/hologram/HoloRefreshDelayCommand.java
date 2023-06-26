package lol.pyr.znpcsplus.commands.hologram;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;

import java.util.Collections;
import java.util.List;

public class HoloRefreshDelayCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public HoloRefreshDelayCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " holo refreshdelay <id> <delay>");
        HologramImpl hologram = context.parse(NpcEntryImpl.class).getNpc().getHologram();
        double delay = context.parse(Double.class);
        hologram.setRefreshDelay((long) (delay * 1000));
        context.send("NPC refresh delay set!");
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) {
            HologramImpl hologram = context.suggestionParse(0, NpcEntryImpl.class).getNpc().getHologram();
            return context.suggestLiteral(String.valueOf(((double) hologram.getRefreshDelay()) / 1000));
        }
        return Collections.emptyList();
    }
}
