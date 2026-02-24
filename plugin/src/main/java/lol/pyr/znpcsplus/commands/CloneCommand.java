package lol.pyr.znpcsplus.commands;

import java.util.Collections;
import java.util.List;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class CloneCommand implements CommandHandler {
  private final NpcRegistryImpl npcRegistry;

  public CloneCommand(NpcRegistryImpl npcRegistry) {
    this.npcRegistry = npcRegistry;
  }

  @Override
  public void run(CommandContext context) throws CommandExecutionException {
    context.setUsage(context.getLabel() + " clone <id> <new id>");
    Player player = context.ensureSenderIsPlayer();

    String id = context.popString();
    if (npcRegistry.getById(id) == null)
      context.halt(Component.text("NPC with ID " + id + " does not exist.", NamedTextColor.RED));
    String newId = context.popString();
    if (npcRegistry.getById(newId) != null)
      context.halt(Component.text("NPC with ID " + newId + " already exists.", NamedTextColor.RED));

    npcRegistry.clone(id, newId, player.getWorld(), new NpcLocation(player.getLocation()));

    context.send(
        Component.text("Cloned NPC with ID " + id + " to ID " + newId + ".", NamedTextColor.GREEN));
  }

  @Override
  public List<String> suggest(CommandContext context) throws CommandExecutionException {
    if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
    return Collections.emptyList();
  }
}
