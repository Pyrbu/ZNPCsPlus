package lol.pyr.znpcsplus.commands;

import java.util.Collections;
import java.util.List;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.FoliaUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandHandler {
  private final NpcRegistryImpl npcRegistry;

  public TeleportCommand(NpcRegistryImpl npcRegistry) {
    this.npcRegistry = npcRegistry;
  }

  @Override
  public void run(CommandContext context) throws CommandExecutionException {
    context.setUsage(context.getLabel() + " teleport <id>");
    Player player = context.ensureSenderIsPlayer();
    NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
    Location location = npc.getBukkitLocation();
    if (location == null) context.halt("Unable to teleport to NPC, the world is not loaded!");
    FoliaUtil.teleport(player, location);
    context.send(Component.text("Teleported to NPC!", NamedTextColor.GREEN));
  }

  @Override
  public List<String> suggest(CommandContext context) throws CommandExecutionException {
    if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
    return Collections.emptyList();
  }
}
