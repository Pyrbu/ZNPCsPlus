package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class ListCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public ListCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        TextComponent.Builder component = Component.text("Npc's:\n").color(NamedTextColor.GOLD).toBuilder();
        for (String id : npcRegistry.getModifiableIds()) {
            NpcImpl npc = npcRegistry.get(id).getNpc();
            NpcLocation location = npc.getLocation();
            component.append(Component.text("ID: " + id, NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("Type: ", NamedTextColor.GREEN))
                    .append(Component.text(npc.getType().getName(), NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("Name: ", NamedTextColor.GREEN))
                    .append(npc.getHologram().getLineComponent(0).color(NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("Location: " + npc.getWorldName() + " X:" + location.getBlockX() + " Y:" + location.getBlockY() + " Z:" + location.getBlockZ(), NamedTextColor.GREEN))
                    .append(Component.text(" | ", NamedTextColor.GRAY))
                    .append(Component.text("[TELEPORT]", NamedTextColor.DARK_GREEN).clickEvent(ClickEvent.runCommand("/npc teleport " + id)))
                    .append(Component.text("\n", NamedTextColor.GRAY));
        }
        context.send(component.build());
    }
}
