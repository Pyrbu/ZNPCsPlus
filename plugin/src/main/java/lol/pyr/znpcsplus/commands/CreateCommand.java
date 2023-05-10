package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import lol.pyr.znpcsplus.util.ZLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CreateCommand implements CommandHandler {
    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " create <npc_id> <npc_type> <npc_name>");
        Player player = context.ensureSenderIsPlayer();
        
        String id = context.popString();
        if (NpcRegistryImpl.get().get(id) != null) context.halt(Component.text("NPC with that ID already exists.", NamedTextColor.RED));

        NpcTypeImpl type = context.parse(NpcTypeImpl.class);
        String name = context.popString();

        NpcEntryImpl entry = NpcRegistryImpl.get().create(id, player.getWorld(), type, new ZLocation(player.getLocation()));
        entry.enableEverything();

        NpcImpl npc = entry.getNpc();
        if (type == NpcTypeImpl.PLAYER) PrefetchedDescriptor.forPlayer(name).thenAccept(skin -> npc.setProperty(EntityPropertyImpl.SKIN, skin));
        npc.getHologram().addLine(Component.text(name));

        context.send(Component.text("Created NPC with ID " + id + " and name " + name + ".", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(NpcRegistryImpl.get().modifiableIds());
        if (context.argSize() == 2) return context.suggestStream(NpcTypeImpl.values().stream().map(NpcTypeImpl::getName));
        return Collections.emptyList();
    }
}
