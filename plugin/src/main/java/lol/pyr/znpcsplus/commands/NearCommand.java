package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class NearCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public NearCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        Player player = context.ensureSenderIsPlayer();
        int raw = context.parse(Integer.class);
        double radius = Math.pow(raw, 2);

        String npcs = npcRegistry.allModifiable().stream()
                        .filter(entry -> entry.getNpc().getBukkitLocation().distanceSquared(player.getLocation()) < radius)
                        .map(NpcEntryImpl::getId)
                        .collect(Collectors.joining(", "));

        if (npcs.length() == 0) context.halt(Component.text("There are no npcs within " + raw + " blocks around you.", NamedTextColor.RED));
        context.send(Component.text("All NPCs that are within " + raw + " blocks from you:", NamedTextColor.GREEN).appendNewline()
                .append(Component.text(npcs, NamedTextColor.GREEN)));
    }
}
