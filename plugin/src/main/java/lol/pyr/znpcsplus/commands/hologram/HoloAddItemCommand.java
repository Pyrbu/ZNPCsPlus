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

public class HoloAddItemCommand implements CommandHandler {
    private final NpcRegistryImpl registry;

    public HoloAddItemCommand(NpcRegistryImpl registry) {
        this.registry = registry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " holo additem <id>");
        Player player = context.ensureSenderIsPlayer();
        org.bukkit.inventory.ItemStack itemStack = player.getInventory().getItemInHand();
        if (itemStack == null) context.halt(Component.text("You must be holding an item!", NamedTextColor.RED));
        HologramImpl hologram = context.parse(NpcEntryImpl.class).getNpc().getHologram();
        hologram.addItemLineStack(itemStack);
        context.send(Component.text("NPC item line added!", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(registry.getModifiableIds());
        return Collections.emptyList();
    }
}
