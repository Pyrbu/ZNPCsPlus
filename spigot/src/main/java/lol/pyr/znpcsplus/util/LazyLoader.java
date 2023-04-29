package lol.pyr.znpcsplus.util;

public class LazyLoader <T> {
    private final ObjectProvider<T> provider;
    private T value;

    private LazyLoader(ObjectProvider<T> provider) {
        this.provider = provider;
    }

    public T get() {
        if (value == null) value = provider.provide();
        return value;
    }

    public static <T> LazyLoader<T> of(ObjectProvider<T> provider) {
        return new LazyLoader<>(provider);
    }

    @FunctionalInterface
    public interface ObjectProvider<T> {
        T provide();
    }
}
