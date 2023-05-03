package lol.pyr.znpcsplus.util.list;

import java.util.*;

public class ListUtil {
    @SafeVarargs
    public static <T> List<T> immutableList(T... elements) {
        return new ImmutableArrayList<>(elements);
    }
}
