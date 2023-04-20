package io.github.znetworkw.znpcservers.reflection;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClassCache {
    protected static final ConcurrentMap<CacheKey, Object> CACHE = new ConcurrentHashMap<>();

    public static Object find(String name, Class<?> objectClass) {
        return CACHE.get(new CacheKey(name, objectClass));
    }

    public static void register(String name, Object object, Class<?> objectClass) {
        CACHE.putIfAbsent(new CacheKey(name, objectClass), object);
    }

    private static class CacheKey {
        private final Class<?> type;
        private final String value;

        public CacheKey(String value, Class<?> type) {
            this.type = type;
            this.value = value;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey classKey = (CacheKey) o;
            return (Objects.equals(this.type, classKey.type) && Objects.equals(this.value, classKey.value));
        }

        public int hashCode() {
            return Objects.hash(this.type, this.value);
        }
    }
}