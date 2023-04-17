package io.github.znetworkw.znpcservers.utility;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class GuavaCollectors {
    public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
        return Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf);
    }

    public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
        return Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf);
    }

    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
        return Collectors.collectingAndThen(Collectors.toMap(keyFunction, valueFunction), ImmutableMap::copyOf);
    }
}
