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

public class LoadAllCommand implements CommandHandler {
  private final NpcRegistryImpl npcRegistry;

  public LoadAllCommand(NpcRegistryImpl npcRegistry) {
    this.npcRegistry = npcRegistry;
  }

  @Override
  public void run(CommandContext context) throws CommandExecutionException {
    FutureUtil.exceptionPrintingRunAsync(
        () -> {
          npcRegistry.reload();
          context.send(
              Component.text("All NPCs have been re-loaded from storage", NamedTextColor.GREEN));
        });
  }

  @Override
  public List<String> suggest(CommandContext context) throws CommandExecutionException {
    return Collections.emptyList();
  }
}
