package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;

import java.util.List;

public class ListCommand implements CommandHandler {

    @Override
    public void run(CommandContext commandContext) throws CommandExecutionException {
        TextComponent.Builder component = Component.text("Npc's:\n").color(NamedTextColor.GOLD).toBuilder();
        for (String id : lol.pyr.znpcsplus.npc.NpcRegistryImpl.get().ids().stream().filter(s -> !s.startsWith(ZNpcsPlus.DEBUG_NPC_PREFIX)).toArray(String[]::new)) {
            NpcEntryImpl npcEntry = NpcRegistryImpl.get().get(id);
            Location location = npcEntry.getNpc().getLocation().toBukkitLocation(npcEntry.getNpc().getWorld());
            component.append(Component.text("ID: " + id, NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("Type: ", NamedTextColor.GREEN))
                    .append(Component.text(npcEntry.getNpc().getType().getName(), NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("Name: ", NamedTextColor.GREEN))
                    .append(npcEntry.getNpc().getHologram().getLine(0).color(NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("Location: " + npcEntry.getNpc().getWorld().getName() + " X:" + location.getBlockX() + " Y:" + location.getBlockY() + " Z:" + location.getBlockZ(), NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("[TELEPORT]", NamedTextColor.DARK_GREEN).clickEvent(ClickEvent.runCommand("/npc teleport " + id)))
                    .append(Component.text("\n", NamedTextColor.GRAY));
        }
        ZNpcsPlus.ADVENTURE.sender(commandContext.getSender()).sendMessage(component.build());
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        return CommandHandler.super.suggest(context);
    }
}
