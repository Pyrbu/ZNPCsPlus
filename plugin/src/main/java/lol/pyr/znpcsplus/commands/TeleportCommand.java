package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TeleportCommand implements CommandHandler {
    @Override
    public void run(CommandContext commandContext) throws CommandExecutionException {
        if (!(commandContext.getSender() instanceof Player)) {
            commandContext.getSender().sendMessage("Only players can use this command!");
            return;
        }
        Player player = (Player) commandContext.getSender();
        if (commandContext.argSize() == 0) {
            ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("Usage: /npc teleport <npc_id>", NamedTextColor.RED));
            return;
        }
        NpcEntryImpl npcEntry = NpcRegistryImpl.get().get(commandContext.popString());
        if (npcEntry == null) {
            ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("NPC not found!", NamedTextColor.RED));
            return;
        }
        player.teleport(npcEntry.getNpc().getLocation().toBukkitLocation(npcEntry.getNpc().getWorld()));
        ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("Teleported to NPC!", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) {
            return context.suggestCollection(NpcRegistryImpl.get().ids().stream().filter(s -> !s.startsWith(ZNpcsPlus.DEBUG_NPC_PREFIX)).collect(Collectors.toList()));
        }
        return CommandHandler.super.suggest(context);
    }
}
