package io.github.znetworkw.znpcservers.reflection;

import io.github.znetworkw.znpcservers.utility.Utils;
import lol.pyr.znpcsplus.ZNPCsPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ReflectionLazyLoader<T> {
    protected final List<String> possibleClassNames;
    protected List<Class<?>> reflectionClasses = new ArrayList<>();
    protected final boolean strict;
    private T cached;
    private boolean loaded = false;

    protected ReflectionLazyLoader(ReflectionBuilder builder) {
        this(builder.getClassNames(), builder.isStrict());
    }

    protected ReflectionLazyLoader(List<String> possibleClassNames, boolean strict) {
        this.possibleClassNames = possibleClassNames;
        this.strict = strict;
        for (String name : possibleClassNames) try {
            reflectionClasses.add(Class.forName(name));
        } catch (ClassNotFoundException ignored) {}
    }

    public T get() {
        if (this.loaded) return this.cached;
        try {
            if (this.reflectionClasses.size() == 0) throw new ClassNotFoundException("No class found: " + possibleClassNames);
            T eval = (this.cached != null) ? this.cached : (this.cached = load());
            if (eval == null) throw new RuntimeException("Returned value is null");
        } catch (Throwable throwable) {
            if (strict) {
                warn(" ----- REFLECTION FAILURE DEBUG INFORMATION, REPORT THIS ON OUR GITHUB ----- ");
                warn(getClass().getSimpleName() + " failed!");
                warn("Class Names: " + possibleClassNames);
                warn("Reflection Type: " + getClass().getCanonicalName());
                warn("Bukkit Version: " + Utils.BUKKIT_VERSION + " (" + Utils.getBukkitPackage() + ")");
                printDebugInfo(this::warn);
                warn("Exception:");
                throwable.printStackTrace();
                warn(" ----- REFLECTION FAILURE DEBUG INFORMATION, REPORT THIS ON OUR GITHUB ----- ");
            }
        }
        this.loaded = true;
        return this.cached;
    }

    private void warn(String message) {
        ZNPCsPlus.LOGGER.warning("[Reflection] " + message);
    }

    protected abstract T load() throws Exception;
    protected void printDebugInfo(Consumer<String> logger) {}
}