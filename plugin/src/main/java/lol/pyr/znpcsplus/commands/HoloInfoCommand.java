package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.hologram.HologramLine;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class HoloInfoCommand implements CommandHandler {
    @Override
    public void run(CommandContext commandContext) throws CommandExecutionException {
        if (!(commandContext.getSender() instanceof Player)) {
            commandContext.getSender().sendMessage("Only players can use this command!");
            return;
        }
        Player player = (Player) commandContext.getSender();
        if (commandContext.argSize() < 1) {
            ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("Usage: /npc holo info <npc_id>", NamedTextColor.RED));
            return;
        }
        String id = commandContext.popString();
        NpcEntryImpl npcEntry = NpcRegistryImpl.get().get(id);
        if (npcEntry == null) {
            ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("NPC not found!", NamedTextColor.RED));
            return;
        }
        List<HologramLine> lines = npcEntry.getNpc().getHologram().getLines();
        TextComponent component = Component.text("NPC Hologram Info of ID " + npcEntry.getId() + ":", NamedTextColor.GREEN);
        component = component.append(Component.newline());
        for (HologramLine line : lines) {
            component = component.append(line.getText());
            component = component.append(Component.newline());
        }
        ZNpcsPlus.ADVENTURE.player(player).sendMessage(component);
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) {
            return context.suggestCollection(NpcRegistryImpl.get().ids().stream().filter(s -> !s.startsWith(ZNpcsPlus.DEBUG_NPC_PREFIX)).collect(Collectors.toList()));
        }
        return CommandHandler.super.suggest(context);
    }
}
