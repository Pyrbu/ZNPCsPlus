package lol.pyr.znpcsplus.commands.hologram;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.hologram.HologramImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class HoloSetItemCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public HoloSetItemCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " holo setitem <id> <line>");
        HologramImpl hologram = context.parse(NpcEntryImpl.class).getNpc().getHologram();
        int line = context.parse(Integer.class);
        if (line < 0 || line >= hologram.getLines().size()) context.halt(Component.text("Invalid line number!", NamedTextColor.RED));
        Player player = context.ensureSenderIsPlayer();
        org.bukkit.inventory.ItemStack itemStack = player.getInventory().getItemInHand();
        if (itemStack == null) context.halt(Component.text("You must be holding an item!", NamedTextColor.RED));
        hologram.removeLine(line);
        hologram.insertItemLineStack(line, itemStack);
        context.send(Component.text("NPC item line set!", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() >= 2) {
            HologramImpl hologram = context.suggestionParse(0, NpcEntryImpl.class).getNpc().getHologram();
            if (context.argSize() == 2) return context.suggestStream(Stream.iterate(0, n -> n + 1)
                    .limit(hologram.getLines().size()).map(String::valueOf));
        }
        return Collections.emptyList();
    }
}
