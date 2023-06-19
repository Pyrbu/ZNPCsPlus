package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CreateCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;
    private final NpcTypeRegistryImpl typeRegistry;

    public CreateCommand(NpcRegistryImpl npcRegistry, NpcTypeRegistryImpl typeRegistry) {
        this.npcRegistry = npcRegistry;
        this.typeRegistry = typeRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " create <id> <type>");
        Player player = context.ensureSenderIsPlayer();
        
        String id = context.popString();
        if (npcRegistry.getById(id) != null) context.halt(Component.text("NPC with that ID already exists.", NamedTextColor.RED));
        NpcTypeImpl type = context.parse(NpcTypeImpl.class);

        NpcEntryImpl entry = npcRegistry.create(id, player.getWorld(), type, new NpcLocation(player.getLocation()));
        entry.enableEverything();

        context.send(Component.text("Created a " + type.getName() + " NPC with ID " + id + ".", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestStream(typeRegistry.getAllImpl().stream().map(NpcTypeImpl::getName));
        return Collections.emptyList();
    }
}
