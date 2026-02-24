package lol.pyr.znpcsplus.api.interaction;

@SuppressWarnings("unused")
public interface ActionFactory {
  InteractionAction createConsoleCommandAction(
      String command, InteractionType interactionType, long cooldown, long delay);

  InteractionAction createMessageAction(
      String message, InteractionType interactionType, long cooldown, long delay);

  InteractionAction createPlayerChatAction(
      String message, InteractionType interactionType, long cooldown, long delay);

  InteractionAction createPlayerCommandAction(
      String command, InteractionType interactionType, long cooldown, long delay);

  InteractionAction createSwitchServerAction(
      String server, InteractionType interactionType, long cooldown, long delay);
}
