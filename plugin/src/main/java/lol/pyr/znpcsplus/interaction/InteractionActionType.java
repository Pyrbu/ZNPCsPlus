package lol.pyr.znpcsplus.interaction;

public interface InteractionActionType<T> {
    String serialize(T obj);
    T deserialize(String str);
    Class<T> getActionClass();
}
