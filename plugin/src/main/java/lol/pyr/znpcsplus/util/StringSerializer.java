package lol.pyr.znpcsplus.util;

public interface StringSerializer<T> {
    String serialize(T obj);
    T deserialize(String str);
}
