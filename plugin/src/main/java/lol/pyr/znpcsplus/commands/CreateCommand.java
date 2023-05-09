package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import lol.pyr.znpcsplus.skin.descriptor.FetchingDescriptor;
import lol.pyr.znpcsplus.util.ZLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CreateCommand implements CommandHandler {
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        if (!(context.getSender() instanceof Player)) {
            ZNpcsPlus.ADVENTURE.sender(context.getSender()).sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return;
        }
        Player player = (Player) context.getSender();
        if (context.argSize() < 3) {
            ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("Usage: /npc create <npc_id> <npc_type> <npc_name>", NamedTextColor.RED));
            return;
        }
        String npcId = context.popString();
        if (npcId.toLowerCase().startsWith(ZNpcsPlus.DEBUG_NPC_PREFIX)) {
            ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("You can not create an Npc with that ID", NamedTextColor.RED));
            return;
        }
        NpcEntryImpl npcEntry = NpcRegistryImpl.get().get(npcId);
        if (npcEntry != null) {
            ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("NPC with that ID already exists.", NamedTextColor.RED));
            return;
        }
        NpcTypeImpl npcType = NpcTypeImpl.byName(context.popString().toUpperCase());
        if (npcType == null) {
            ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("Invalid NPC type.", NamedTextColor.RED));
            return;
        }
        String npcName = context.popString();
        NpcEntryImpl npcEntry1 = NpcRegistryImpl.get().create(npcId, player.getWorld(), npcType, new ZLocation(player.getLocation()));
        npcEntry1.enableEverything();
        NpcImpl npc = npcEntry1.getNpc();
        if (npcType == NpcTypeImpl.PLAYER) {
            npc.setProperty(EntityPropertyImpl.SKIN, new FetchingDescriptor(npcName));
        }
        npc.getHologram().addLine(Component.text(npcName));
        ZNpcsPlus.ADVENTURE.player(player).sendMessage(Component.text("Created NPC with ID " + npcId + " and name " + npcName + ".", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) {
            return context.suggestLiteral("<npc_id>");
        }
        if (context.argSize() == 2) {
            return context.suggestCollection(NpcTypeImpl.values().stream().map(NpcTypeImpl::getName).collect(Collectors.toList()));
        }
        if (context.argSize() == 3) {
            return context.suggestLiteral("<npc_name>");
        }
        return CommandHandler.super.suggest(context);
    }
}
