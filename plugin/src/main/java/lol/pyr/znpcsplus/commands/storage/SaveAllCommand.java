package lol.pyr.znpcsplus.commands.storage;

import java.util.Collections;
import java.util.List;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.util.FutureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SaveAllCommand implements CommandHandler {
  private final NpcRegistryImpl npcRegistry;

  public SaveAllCommand(NpcRegistryImpl npcRegistry) {
    this.npcRegistry = npcRegistry;
  }

  @Override
  public void run(CommandContext context) throws CommandExecutionException {
    FutureUtil.exceptionPrintingRunAsync(
        () -> {
          npcRegistry.save();
          context.send(Component.text("All NPCs have been saved to storage", NamedTextColor.GREEN));
        });
  }

  @Override
  public List<String> suggest(CommandContext context) throws CommandExecutionException {
    return Collections.emptyList();
  }
}
