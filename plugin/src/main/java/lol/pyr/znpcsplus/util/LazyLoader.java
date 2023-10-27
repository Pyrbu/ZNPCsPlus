package lol.pyr.znpcsplus.util;

import java.util.function.Supplier;

public class LazyLoader <T> {
    private final Supplier<T> supplier;
    private T value;

    private LazyLoader(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (value == null) value = supplier.get();
        return value;
    }

    public static <T> LazyLoader<T> of(Supplier<T> supplier) {
        return new LazyLoader<>(supplier);
    }
}
