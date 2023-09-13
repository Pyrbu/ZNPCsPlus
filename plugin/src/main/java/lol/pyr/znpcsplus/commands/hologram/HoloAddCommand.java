package lol.pyr.znpcsplus.commands.hologram;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.hologram.HologramItem;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;

public class HoloAddCommand implements CommandHandler {
    private final NpcRegistryImpl registry;

    public HoloAddCommand(NpcRegistryImpl registry) {
        this.registry = registry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " holo add <id> <text>");
        HologramImpl hologram = context.parse(NpcEntryImpl.class).getNpc().getHologram();
        context.ensureArgsNotEmpty();
        String in = context.dumpAllArgs();
        if (in.toLowerCase().startsWith("item:")) {
            if (!HologramItem.ensureValidItemInput(in.substring(5))) {
                context.halt(Component.text("The item input is invalid!", NamedTextColor.RED));
            }
        }
        hologram.addLine(in);
        context.send(Component.text("NPC line added!", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(registry.getModifiableIds());
        return Collections.emptyList();
    }
}
