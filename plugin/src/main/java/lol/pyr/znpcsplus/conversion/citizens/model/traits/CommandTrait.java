package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import java.util.Set;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.conversion.citizens.model.SectionCitizensTrait;
import lol.pyr.znpcsplus.interaction.consolecommand.ConsoleCommandAction;
import lol.pyr.znpcsplus.interaction.playercommand.PlayerCommandAction;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class CommandTrait extends SectionCitizensTrait {
  private final TaskScheduler scheduler;

  public CommandTrait(TaskScheduler scheduler) {
    super("commandtrait");
    this.scheduler = scheduler;
  }

  @Override
  public @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section) {
    ConfigurationSection commands = section.getConfigurationSection("commands");
    if (commands != null) {
      Set<String> keys = commands.getKeys(false);
      if (keys != null) {
        for (String key : keys) {
          ConfigurationSection commandSection = commands.getConfigurationSection(key);
          String command = commandSection.getString("command");
          String hand = commandSection.getString("hand", "BOTH");
          InteractionType clickType = wrapClickType(hand);
          boolean isPlayerCommand = commandSection.getBoolean("player", true);
          int cooldown = commandSection.getInt("cooldown", 0);
          int delay = commandSection.getInt("delay", 0);
          if (command != null) {
            InteractionAction action;
            if (isPlayerCommand) {
              action = new PlayerCommandAction(scheduler, command, clickType, cooldown, delay);
            } else {
              action = new ConsoleCommandAction(scheduler, command, clickType, cooldown, delay);
            }
            npc.addAction(action);
          }
        }
      }
    }
    return npc;
  }

  private InteractionType wrapClickType(String hand) {
    if (hand == null) {
      return InteractionType.ANY_CLICK;
    }
    switch (hand) {
      case "RIGHT":
      case "SHIFT_RIGHT":
        return InteractionType.RIGHT_CLICK;
      case "LEFT":
      case "SHIFT_LEFT":
        return InteractionType.LEFT_CLICK;
      case "BOTH":
        return InteractionType.ANY_CLICK;
    }
    throw new IllegalStateException();
  }
}
