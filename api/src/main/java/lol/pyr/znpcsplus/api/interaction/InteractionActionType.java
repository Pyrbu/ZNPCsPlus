package lol.pyr.znpcsplus.api.interaction;

public interface InteractionActionType<T> {
  String serialize(T obj);

  T deserialize(String str);

  Class<T> getActionClass();
}
