package lol.pyr.znpcsplus.interaction;

import lol.pyr.znpcsplus.api.interaction.ActionFactory;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.consolecommand.ConsoleCommandAction;
import lol.pyr.znpcsplus.interaction.message.MessageAction;
import lol.pyr.znpcsplus.interaction.playerchat.PlayerChatAction;
import lol.pyr.znpcsplus.interaction.playercommand.PlayerCommandAction;
import lol.pyr.znpcsplus.interaction.switchserver.SwitchServerAction;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.BungeeConnector;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ActionFactoryImpl implements ActionFactory {
  private final TaskScheduler scheduler;
  private final BukkitAudiences adventure;
  private final LegacyComponentSerializer textSerializer;
  private final BungeeConnector bungeeConnector;

  public ActionFactoryImpl(
      TaskScheduler scheduler,
      BukkitAudiences adventure,
      LegacyComponentSerializer textSerializer,
      BungeeConnector bungeeConnector) {
    this.scheduler = scheduler;
    this.adventure = adventure;
    this.textSerializer = textSerializer;
    this.bungeeConnector = bungeeConnector;
  }

  public InteractionAction createConsoleCommandAction(
      String command, InteractionType interactionType, long cooldown, long delay) {
    return new ConsoleCommandAction(this.scheduler, command, interactionType, cooldown, delay);
  }

  public InteractionAction createMessageAction(
      String message, InteractionType interactionType, long cooldown, long delay) {
    return new MessageAction(
        this.adventure, textSerializer, message, interactionType, cooldown, delay);
  }

  public InteractionAction createPlayerChatAction(
      String message, InteractionType interactionType, long cooldown, long delay) {
    return new PlayerChatAction(this.scheduler, message, interactionType, cooldown, delay);
  }

  public InteractionAction createPlayerCommandAction(
      String command, InteractionType interactionType, long cooldown, long delay) {
    return new PlayerCommandAction(this.scheduler, command, interactionType, cooldown, delay);
  }

  public InteractionAction createSwitchServerAction(
      String server, InteractionType interactionType, long cooldown, long delay) {
    return new SwitchServerAction(bungeeConnector, server, interactionType, cooldown, delay);
  }
}
