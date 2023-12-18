package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class NearCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public NearCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " near <radius>");
        Player player = context.ensureSenderIsPlayer();
        int raw = context.parse(Integer.class);
        double radius = Math.pow(raw, 2);

        List<NpcEntryImpl> entries = npcRegistry.getAllModifiable().stream()
                .filter(entry -> entry.getNpc().getWorld().equals(player.getWorld()))
                .filter(entry -> entry.getNpc().getBukkitLocation().distanceSquared(player.getLocation()) < radius)
                .collect(Collectors.toList());

        if (entries.isEmpty()) context.halt(Component.text("There are no npcs within " + raw + " blocks around you.", NamedTextColor.RED));

        Component component = Component.text("All NPCs that are within " + raw + " blocks from you:", NamedTextColor.GOLD).appendNewline();
        for (NpcEntryImpl entry : entries) {
            NpcImpl npc = entry.getNpc();
            NpcLocation location = npc.getLocation();
            component = component.append(Component.text("ID: " + entry.getId(), npc.isEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("Type: ", NamedTextColor.GREEN))
                    .append(Component.text(npc.getType().getName(), NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("Location: " + npc.getWorldName() + " X:" + location.getBlockX() + " Y:" + location.getBlockY() + " Z:" + location.getBlockZ(), NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("[TELEPORT]", NamedTextColor.DARK_GREEN).clickEvent(ClickEvent.runCommand("/znpcs teleport " + entry.getId())))
                    .appendNewline();
        }
        context.send(component);
    }
}
