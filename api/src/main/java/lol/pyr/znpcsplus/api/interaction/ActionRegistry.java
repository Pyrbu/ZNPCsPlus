package lol.pyr.znpcsplus.api.interaction;

public interface ActionRegistry {
  void register(InteractionActionType<?> type);

  void unregister(Class<? extends InteractionAction> clazz);
}
